
$(function () {

  $('.read-more').click(function() {
    $('#' + $(this).attr('href')).toggle(); 
    return false;
  });

});
