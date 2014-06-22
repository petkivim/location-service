$(document).ready( function() {
    $(':input:visible:enabled:first').focus();

    $("#btn_back_user").click( function( e ) {
        var usr = $("#select_user").val();
        var url = "ladmuser.htm";
        if(typeof usr !== 'undefined')
        {
            url += "?select_user=" + usr;
        }
        $(location).attr('href',url);
    });
});