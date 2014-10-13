require "sinatra"

get "/" do
  erb :index
end

get "/hosts" do
  send_file "public/hosts/faq.html"
end

get "/hosts/" do
  send_file "public/hosts/faq.html"
end
