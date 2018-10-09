$(document).ready(function(){
    $('tbody tr').click(function(){
        window.location = $(this).data('href');
        return false;
    });
    $('tbody tr').css('cursor', 'pointer');
});