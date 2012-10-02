# coderetreat.com

The official site for the Global Day of Coderetreat.

This is a jekyll based site running as a Sinatra app (for heroku deployment and future functionality)

* Jekyll: [http://github.com/mojombo/jekyll](http://github.com/mojombo/jekyll)
* Sinatra Setup: [http://jstorimer.com/2009/12/29/jekyll-on-heroku.html](http://jstorimer.com/2009/12/29/jekyll-on-heroku.html)

## Setup

    bundle install

## Running Locally

    cd public
    jekyll --server --auto

Browse: to http://localhost:4000

## Deployment

### Staging

    git push heroku master

Browse: http://globalday.coderetreat.org
