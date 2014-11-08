require "sinatra"
require "json"
require "sass"

get "/" do
  erb :index
end

get "/hosts" do
  erb :hosts
end

get "/style.css" do
  scss :style
end

get "/print.css" do
  scss :print
end

def serve_pages pages
  pages.each { |page| serve_page page }
end

def serve_page path
  get("/#{path}") { erb template_for_path(path) }
  get("/#{path}.html") { erb template_for_path(path) }
end

def template_for_path path
  path.split("/").last.to_sym
end

serve_pages(["hosts",
             "hosts/faq",
             "hosts/guide",
             "timeline"
])

def event_count
  JSON.parse(File.read("public/data/locations.json")).count
end

class Sponsor
  def initialize name, url
    @name, @url = name, url
  end

  def logo_path
    "images/sponsors/#{name.downcase}_logo.png"
  end

  attr_reader :name, :url
end

def galaxy_sponsors
  [
    Sponsor.new('ThoughtWorks', 'http://www.thoughtworks.com'),
  ]
end

def globe_sponsors
  [
    Sponsor.new('Github', 'https://github.com'),
  ]
end

def continent_sponsors
  [
    Sponsor.new('DNSimple', 'https://www.dnsimple.com/'),
  ]
end
