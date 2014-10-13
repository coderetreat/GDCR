require "sinatra"

get "/" do
  erb :index
end

get "/hosts" do
  erb :faq
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
             "hosts/media_pack",
             "survey",
             "sponsors",
])
