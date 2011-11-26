
$(function() {

//$('a#save-our-driveway').lightBox();

//$('a#blue-ridge-pic').lightBox(); 

  $('a.fancybox').fancybox({ 
    'hideOnContentClick': true, 
    'overlayShow': false,
    'zoomSpeedIn': 600,
    'zoomSpeedOut': 500,
    'easingIn': 'easeOutBack',
    'easingOut': 'easeInBack',
    'overlayColor': '#fff'
  });

  $('.iframe').fancybox({
    'width': 800,
    'height': 600
  });

});

