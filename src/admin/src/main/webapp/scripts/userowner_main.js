$(document).ready( function() {
    var $dialog_user = $('<div></div>')
    .html('Are you sure you want to delete the selected user permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete user',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_user"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    var $dialog_owner = $('<div></div>')
    .html('Are you sure you want to delete the selected owner permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete owner',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_owner"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    $(':input:visible:enabled:first').focus();

    $('input[name="delete_user"]').click( function() {
        $dialog_user.dialog('open');
    });

    $('input[name="delete_owner"]').click( function() {
        $dialog_owner.dialog('open');
    });

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