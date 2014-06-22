$(document).ready( function() {
    var $dialog = $('<div></div>')
    .html('Are you sure you want to delete the selected subject matter permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete subject matter',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_subject"]').click();
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