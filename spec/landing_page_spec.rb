require 'spec_helper'

describe "Landing Page" do
  it "Displays a title" do
    visit '/'
    expect(page).to have_content('Global Day of Coderetreat')
  end

  it "displays sponsors"
  it "displays links to hosting resources"

  it "displays social media links"
  it "displays links to the coderetreat.org site"

  it "describes CoderDojo" do
    visit "/"
    pending
    expect(page).to have_content("CoderDojo")
  end

  describe "#event_count" do
    it "Displays a count of the cities in the locations.json file" do
      expect(File).to receive(:read).with("public/data/locations.json").
        and_return( [{city: "Chicago", coords: [1, 2]},
                     {city: "Shanghai", coords: [3, 4]}].to_json)

        visit '/'

        expect(page).to have_content("2 registered events")
    end
  end
end
