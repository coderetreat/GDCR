use Rack::Static, urls: { "/" => "index.html", "/hosts/" => "hosts/faq.html", "/hosts" => "hosts/faq.html" }, root: "public"

run Rack::URLMap.new({
  "/" => Rack::Directory.new("public"),
})


