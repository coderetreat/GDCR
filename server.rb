require "sinatra"

get "/" do
  send_file "public/index.html"
end

get "/hosts" do
  send_file "public/hosts/faq.html"
end

get "/hosts/" do
  send_file "public/hosts/faq.html"
end
