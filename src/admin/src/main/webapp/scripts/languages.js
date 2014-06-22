$(document).ready( function() {
    $(':input:visible:enabled:first').focus();

    $("#btn_back_language").click( function( e ) {
        var sub = $("#select_language").val();
        var url = "languages.htm";
        if(typeof sub !== 'undefined')
        {
            url += "?select_language=" + sub;
        }
        $(location).attr('href',url);
    });
});