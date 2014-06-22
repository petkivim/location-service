$(document).ready( function() {
    $(':input:visible:enabled:first').focus();

    $("#btn_back_subject").click( function( e ) {
        var sub = $("#select_subject").val();
        var url = "subjectmatters.htm";
        if(typeof sub !== 'undefined')
        {
            url += "?select_subject=" + sub;
        }
        $(location).attr('href',url);
    });
});