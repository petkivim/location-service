var locations = new Array();
var locations_type_index = new Array();

$(document).ready( function() {
    var $dialog_library = $('<div></div>')
    .html('Are you sure you want to delete the selected library permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete library',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_library"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    var $dialog_collection = $('<div></div>')
    .html('Are you sure you want to delete the selected collection permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete collection',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_collection"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    var $dialog_shelf = $('<div></div>')
    .html('Are you sure you want to delete the selected shelf permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete shelf',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_shelf"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    $("#select_library").change( function() {
        $("#btn_list_library").click();
    });

    $("#select_collection").change( function() {
        $("#btn_list_collection").click();
    });

    $('input[name="delete_library"]').click( function() {
        $dialog_library.dialog('open');
    });

    $('input[name="delete_collection"]').click( function() {
        $dialog_collection.dialog('open');
    });

    $('input[name="delete_shelf"]').click( function() {
        $dialog_shelf.dialog('open');
    });
         
    $('#loading')
    .hide()  // hide it initially
    .ajaxStart(function() {
        $(this).show();
    })
    .ajaxStop(function() {
        $(this).hide();
    });
    
    $( "#location_search" ).focus(function() {
        // Load locations when search field 
        // gets focus the first time
        if(locations.length == 0) {
            get_locations();
        }
    });
    
    $('#search_form').submit(function() {
        if($('#location_id').val() == '' || $('#location_search').val() == '') {
            return false;
        }
        return true;
    });
    
    $( "#location_search" ).autocomplete({
        source: function( request, response ) {
            var matcher = new RegExp( $.ui.autocomplete.escapeRegex( request.term ), "i" );
            response($.map(locations, function(item) {
                if(matcher.test(item['name']) 
                    || matcher.test(item['locationCode']) 
                    || matcher.test(item['collectionCode']) 
                    || matcher.test(item['callno'])) {
                    var collectionCode = '';
                    if(item['collectionCode'].length > 0) {
                        collectionCode = ' (' + item['collectionCode'] + ')';
                    }
                    var type = ' (' + item['type'] + ')';
                    return {
                        label: item['callno'] + collectionCode + type, 
                        value: item['callno'] + collectionCode + type, 
                        id: item['id']
                    };
                }
                return false;
            }));
        },
        minLength: 0,
        select: function( event, ui ) {
            var type = locations_type_index[ui.item.id];
            $('#btn_list_location').attr('name', 'btn_list_' + type);   
            $('#btn_edit_location').attr('name', 'btn_edit_' + type);           
            $('#location_id').attr('name', 'select_' + type);
            $('#location_id').val(ui.item.id);
        }
    });
});

function get_locations() {
    var owner = $('#owner').text();
    $.ajax({
        async: true,
        url: 'Search',
        data: "owner=" + owner + "&level=all",
        dataType: "xml",
        success: function(xml){
            locations = new Array();
            locations_type_index = new Array();
            $(xml).find('location').each(function(){
                var location = new Array();
                location['type'] = $(this).attr('type');
                location['id'] = $(this).find('locationid').first().text();
                location['name'] = $(this).find('name').first().text();
                location['locationCode'] = $(this).find('locationcode').first().text();
                location['collectionCode'] = $(this).find('collectioncode').first().text();
                location['callno'] = $(this).find('callnumber').first().text();
                locations.push(location);
                locations_type_index[location['id']] = location['type'];            
            });
        }
    });
}