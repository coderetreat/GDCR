ENV['RACK_ENV'] = 'test'
require 'rack/test'
require File.expand_path '../../server.rb', __FILE__
require 'capybara'
require 'capybara/dsl'

RSpec.configure do |c|
  c.include Rack::Test::Methods
  c.include Capybara::DSL

  c.before { Capybara.app = Sinatra::Application.new }
  c.before { def app() Sinatra::Application; end } # For Rack::Test
end
