$(document).ready( function() {
    var $dialog_image = $('<div></div>')
    .html('Are you sure you want to delete the selected image permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete image',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_img"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    var $dialog_map = $('<div></div>')
    .html('Are you sure you want to delete the selected map permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete map',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_map"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    $('input[name="delete_img"]').click( function() {
        $dialog_image.dialog('open');
    })

    $('input[name="delete_map"]').click( function() {
        $dialog_map.dialog('open');
    })
});