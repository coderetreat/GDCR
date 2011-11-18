# coderetreat.com

The official site for coderetreat with the venerable Corey Haines.

This is a jekyll based site running as a Sinatra app (for heroku deployment and future functionality)

* Jekyll: [http://github.com/mojombo/jekyll](http://github.com/mojombo/jekyll)
* Sinatra Setup: [http://jstorimer.com/2009/12/29/jekyll-on-heroku.html](http://jstorimer.com/2009/12/29/jekyll-on-heroku.html)

## Setup

    bundle install

## Running Locally

    jekyll --server --auto

Browse: to http://localhost:4000

## Deployment

### Staging

    git push heroku master

Browse: http://coderetreat.heroku.com
