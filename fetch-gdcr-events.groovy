#!/usr/bin/env groovy
// -*- mode: groovy -*-
// vi: set ft=groovy :

if (this.args.length < 1) {
  println("Oops! You need to provide a Google API Key to use this script.")
  println()
  println("USAGE: fetch_gdcr_events.groovy <SERVER GOOGLE API KEY>")
  println()
  println("See https://github.com/googlemaps/google-maps-services-java#api-keys for instructions on how to setup an API key.")
  System.exit(1)
}

def apiKey = this.args[0]
def gdcrDate = "2014-11-15"
def gdcrEventTag = "gdcr14" // the tag used to search the coderetreat.org site for events
def outputCsvFile = "data/locations.csv"
def outputJsonFile = "public/data/locations.json"

@Grapes([
  @Grab('com.google.maps:google-maps-services:0.1.3'),
  @Grab('org.jsoup:jsoup:1.6.1')
])
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import groovy.json.JsonBuilder

import com.google.maps.*
import com.google.maps.model.*

import java.util.TimeZone
import java.util.Date

class Event implements Comparable<Event> {
  final String country
  final String city
  final List<String> urls
  final double[] coords
  final double offset
  final String timeZone

  public Event(String country,
               String city,
               double[] coords,
               double offset,
               String timeZone,
               List<String> urls) {
    this.country = country
    this.city = city
    this.coords = coords
    this.offset = offset
    this.timeZone = timeZone
    this.urls = new ArrayList(urls)
  }

  public Event(params) {
    this.country = params['country']
    this.city = params['city']
    this.coords = params['coords']
    this.offset = params['offset']
    this.timeZone = params['timeZone']
    this.urls = new ArrayList(params['urls'])
  }

  String getName() { return "${city}, ${country}" }

  int compareTo(Event other) {
    country <=> other.country ?: city <=> other.city
  }

  String toString() { "${country}\t${city}" }
  String toCsv() { "${country}\t${city}\t${coords[0]}\t${coords[1]}\t${timeZone}\t${offset}\t${urls[0]}" }
}

class EventsFetcher {

  private final long gdcrDateInMillis
  private final GeoApiContext geoApiContext
  private final gdcrEventTag

  public EventsFetcher(String gdcrDate, String gdcrEventTag, String googleServerApiKey) {
     this.gdcrDateInMillis = convertDateToMillis(gdcrDate)
     this.gdcrEventTag = gdcrEventTag
     this.geoApiContext = new GeoApiContext()

     if (googleServerApiKey)
       this.geoApiContext.setApiKey(googleServerApiKey)
  }

  private long convertDateToMillis(String date) {
     Date.parse("yyyy-MM-dd hh:mm:ssZ", "${date} 00:00:00+0000").getTime()
  }


  List<Event> fetchEvents(int pageNum = 1) {
    List<Event> events = []

    def eventElements = fetchEventElements(pageNum)
    eventElements.each {
      def event = parse(it)
      if (event) events += event
    }

    if (eventElements.size() > 0) events += fetchEvents(pageNum + 1);

    events
  }

  private Elements fetchEventElements(int pageNum) {
    def pageUrl = "http://coderetreat.org/events/event/listByType?type=${gdcrEventTag}&page_q=AAAAAAAAADQ=&page=${pageNum}"
    def doc = Jsoup.connect(pageUrl)
                   .header("Cache-control", "no-cache")
                   .header("Cache-store", "no-store")
                   .get()

    doc.select("ul.clist h3 a")
  }

  private Event parse(Element eventElement) {
      def eventName = stripHeader(eventElement.text())
      def matcher = eventName =~ /(.+),(.+)/

      if (matcher.matches()) {
        def city = matcher[0][1].trim()
        def country = matcher[0][2].trim()
        def coords = fetchCoords(city, country)
        def timeZone = fetchTimeZone(coords)

        def gdcrOffset = timeZone.getOffset(gdcrDateInMillis) / 1000 / 60 / 60
        // / comment to fix syntax highlighting in VIM

        def event = new Event(
          city: city,
          country: country,
          coords: [coords.lat, coords.lng],
          offset: gdcrOffset,
          timeZone: timeZone.getID(),
          urls: [eventElement.attr("href")]
        )
        return event
      } else {
        println "Unable to parse event name: ${eventName}"
        return null
      }
  }

  private String stripHeader(String eventName) {
      eventName.replaceAll("Global Day of Coderetreat 2014 - ", "")
  }

  private LatLng fetchCoords(String city, String country) {
    GeocodingResult[] results =  GeocodingApi.geocode(geoApiContext, "${city}, ${country}").await()
    results[0].geometry.location
  }

  private TimeZone fetchTimeZone(LatLng coords) {
    TimeZoneApi.getTimeZone(geoApiContext, coords).await()
  }

  public List<Event> combineDuplicates(List<Event> events) {
      def mergedEvents = [:]
      events.each {
        if (mergedEvents.containsKey(it.name)) {
          def event = mergedEvents.get(it.name)
          mergedEvents.put(it.name, merge(event, it))
        } else {
          mergedEvents.put(it.name, it)
        }
      }
      new ArrayList(mergedEvents.values())
  }

  private Event merge(Event eventA, Event eventB) {
    def mergedUrls = new ArrayList(eventA.urls)
    mergedUrls.addAll(eventB.urls)

    new Event(
      city: eventA.city,
      country: eventA.country,
      coords: eventA.coords,
      offset: eventA.offset,
      timeZone: eventA.timeZone,
      urls: mergedUrls
    )
  }
}

/* **** Main Script **** */

def fetcher = new EventsFetcher(gdcrDate, gdcrEventTag, apiKey)
def events = fetcher.fetchEvents().sort()

new File(outputCsvFile).withWriter { out ->
  events.each { out.writeLine(it.toCsv()) }
}

def uniqueEvents = fetcher.combineDuplicates(events)
new File(outputJsonFile).write(new JsonBuilder(uniqueEvents).toPrettyString())

