require "yaml"
require "json"

locations_json_path = "public/data/locations.json"
locations_yml_path = "data/locations.yml"

task :geocode_locations_for_map => locations_json_path

file locations_json_path => [locations_yml_path, :initialize_geocoder] do
  cities = YAML.load_file(locations_yml_path).uniq
  locations = cities.map do |city|
    coords = Geocoder.search(city).first.geometry["location"]
    {"city" => city, "coords" => [coords["lat"], coords["lng"]]}
  end
  File.write(locations_json_path, format_json(locations))
end

task :initialize_geocoder do
  require "geocoder"
  Geocoder.configure(timeout: 10)
end

def format_json data
  data.to_json.
    gsub("},", "},\n  ").
    gsub("[{", "[\n  {").
    gsub("}]", "}\n]")
end
