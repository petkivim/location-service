$(document).ready( function() {
    var $dialog = $('<div></div>')
    .html('Are you sure you want to delete the selected language permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete language',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_language"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    $(':input:visible:enabled:first').focus();


    $('input[name="btn_delete"]').click( function() {
        $dialog.dialog('open');
    });

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