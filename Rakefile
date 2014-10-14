require "yaml"
require "geocoder"
require "json"

Geocoder.configure(timeout: 10)

locations_json_path = "public/data/locations.json"
locations_yml_path = "data/locations.yml"

desc "Tasks that need to be run on Heroku before the app can be deployed"
task :default => :locations

task :locations => [locations_json_path]

file locations_json_path => locations_yml_path do
  cities = YAML.load_file(locations_yml_path)
  locations = cities.map do |city|
    puts "Looking for #{city}"
    coords = Geocoder.search(city).first.geometry["location"]
    {"city" => city, "coords" => [coords["lat"], coords["lng"]]}
  end
  locations_json = format_json(locations)
  File.write(locations_json_path, locations_json)
end

def format_json data
  data.to_json.
    gsub("},", "},\n  ").
    gsub("[{", "[\n  {").
    gsub("}]", "}\n]")
end
