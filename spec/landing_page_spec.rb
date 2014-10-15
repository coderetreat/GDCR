require 'spec_helper'

describe "Landing Page" do
  it "Displays a title" do
    visit '/'
    expect(page).to have_content('Global Day of Coderetreat')
  end
end
