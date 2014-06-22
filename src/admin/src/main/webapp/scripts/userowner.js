$(document).ready( function() {
    $(':input:visible:enabled:first').focus();

    $("#btn_back_owner").click( function( e ) {
        var own = $("#select_owner").val();
        var url = "userowner.htm";
        if(typeof own !== 'undefined')
        {
            url += "?select_owner=" + own;
        }
        $(location).attr('href',url);
    });

    $("#btn_back_user").click( function( e ) {
        var usr = $("#select_user").val();
        var url = "userowner.htm";
        if(typeof usr !== 'undefined')
        {
            url += "?select_user=" + usr;
        }
        $(location).attr('href',url);
    });
});