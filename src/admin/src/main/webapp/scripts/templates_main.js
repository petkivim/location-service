var library_id;
var collection_id;
var shelf_id;
var libraries = new Array();
var collections = new Array();
var shelves = new Array();
var collection_codes = new Array();

$(document).ready( function() {
    var $dialog_css = $('<div></div>')
    .html('Are you sure you want to delete the selected style sheet permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete style sheet',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_css"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    var $dialog_template = $('<div></div>')
    .html('Are you sure you want to delete the selected template permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete template',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_template"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    var $dialog_js = $('<div></div>')
    .html('Are you sure you want to delete the selected script permanently?')
    .dialog({
        autoOpen: false,
        resizable: false,
        title: 'Delete script',
        height: 130,
        modal: true,
        buttons: {
            "OK": function() {
                $( this ).dialog( "close" );
                $('input[name="btn_delete_js"]').click();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    var $add_template_dialog = $( ".locations" ).dialog({
        autoOpen: false,
        modal: true,
        width: 600,
        height: 400,
        buttons: {
            Save: function() {
                $( this ).dialog( "close" );
                $('input[name="btn_add_template"]').click();
            },
            Close: function() {
                $( this ).dialog( "close" );
            }
        },
        open: function(event, ui) {          
            $('#radio1').click();
        }
    });
    
    $( "#radio" ).buttonset();

    $("#select_lang").change( function() {
        $("#btn_select_lang").click();
    });
    
    $('input[name="delete_template"]').click( function() {
        $dialog_template.dialog('open');
    });

    $('input[name="delete_css"]').click( function() {
        $dialog_css.dialog('open');
    });

    $('input[name="delete_js"]').click( function() {
        $dialog_js.dialog('open');
    });

    $('#open_add_dialog').click(function() {
        $('input[name="template_old"]').val('');
        $add_template_dialog.dialog( "option", "title", "Add template" );
        $add_template_dialog.dialog('open');
    });

    $('input[name="rename_template"]').click( function() {
        $('input[name="template_old"]').val($('select[name="template"]').val());
        $add_template_dialog.dialog( "option", "title", "Rename template (" + $('select[name="template"] option:selected').text().trim() + ")");
        $add_template_dialog.dialog('open');
    });
    
    /**
     * Fetches a lis of all the libraries and updates the UI.
     */
    get_libraries_list();

    /**
     * When a library is selected, fetches all the collections
     * related to that library and updates the UI.
     */
    $('div.libraries ul li.clickable').live('click', function() {
        var type = $('[name="type"]:checked').val();
        if(type == 'collection' || type == 'shelf') {
            var id = $(this).attr('id');
            var owner = $('#owner').text();
            var url = $('#url').text();
            var html = '';
            $.ajax({
                async: true,
                url: 'Search',
                data: "owner=" + owner + "&level=collection&search=" + id,
                dataType: "xml",
                success: function(xml){
                    $('div.collections ul').remove();
                    $('div.shelves ul').remove();
                    $('div.collections').append('<ul>');
                    collections = new Array();
                    collection_codes = new Array();
                    $(xml).find('location').each(function(){
                        if($(this).attr('type') == 'collection') {
                            var id = $(this).find('locationid').first().text();
                            html = '<li class="clickable" id="' + id + '">';
                            collections[id] = $(this).find('locationcode').first().text();
                            html += $(this).find('name').first().text().trim() + '</li>';
                            $('div.collections ul').append(html);
                            collection_codes[id] = $(this).find('collectioncode').first().text();
                        }
                    });
                    if($('div.collections ul li').size() == 0) {
                        html = '<li>No collections</li>'
                        $('div.collections ul').append(html);
                    }
                }
            });         
        }
    });

    /**
     * When a collection is selected, fetches all the shelves
     * related to that collection and updates the UI.
     */
    $('div.collections ul li.clickable').live('click', function() {
        var type = $('[name="type"]:checked').val();
        if(type == 'shelf') {
            var id = $(this).attr('id');
            var owner = $('#owner').text();
            var html = '';
            $.ajax({
                async: true,
                url: 'Search',
                data: "owner=" + owner + "&level=shelf&search=" + id,
                dataType: "xml",
                success: function(xml){
                    shelves = new Array();
                    $('div.shelves ul').remove();
                    $('div.shelves').append('<ul>');
                    $(xml).find('location').each(function(){
                        if($(this).attr('type') == 'shelf') {
                            var id = $(this).find('locationid').first().text().trim();
                            shelves[id] = $(this).find('locationcode').first().text().trim()
                            html = '<li class="clickable" id="' + id + '">';
                            html += $(this).find('name').first().text().trim() + '</li>';
                            $('div.shelves ul').append(html);
                        }
                    });
                    if($('div.shelves ul li').size() == 0) {
                        html = '<li>No shelves</li>'
                        $('div.shelves ul').append(html);
                    }
                }
            });
        }
    });

    /*
     * Updates the template name label when one of the locations is clicked.
     */
    $('div.locations div ul li.clickable').live('click', function() {
        var id = $(this).attr('id');
        var type = $(this).parent().parent().attr('class');
        var name = '';
        if(type == 'libraries') {
            library_id = id;
            name = libraries[library_id];
        } else if(type == 'collections') {
            collection_id = id;
            name = libraries[library_id] + ' ' + collections[collection_id];
        } else if(type == 'shelves') {
            shelf_id = id;
            name = libraries[library_id] + ' ' + collections[collection_id];
            name += ' ' + shelves[shelf_id];
        }
        
        if(type == 'collections' || type == 'shelves') {
            if(collection_codes[collection_id].length > 0 && $('#include_collection_code').attr('checked')) {
                name += ' ' + collection_codes[collection_id];
            }      
        }
        
        $('#template_name').text(name);
        $('input[name="location_id"]').val(id);
    });

    /*
     * Updates the view when one of the template type buttons
     * is clicked.
     */
    $('[name="type"]').click(function() {
        var value = $(this).val();
        var name = '';
        var display_value = value;
        if(value == 'library') {
            $('input[name="template_other_name"]').hide();
            $('div.locations div[id!="radio"]').show();
            $('div.collections ul').remove();
            $('div.shelves ul').remove();
            name = 'all libraries';
        } else if (value == 'collection') {
            $('input[name="template_other_name"]').hide();
            $('div.locations div[id!="radio"]').show();
            $('div.shelves ul').remove();
            name = 'all collections';
        } else if (value == 'shelf') {
            $('input[name="template_other_name"]').hide();
            $('div.locations div[id!="radio"]').show();
            name = 'all shelves';
        } else {
            $('input[name="template_other_name"]').hide();
            $('div.locations div[id!="radio"]').hide();
            $('div.collections ul').remove();
            $('div.shelves ul').remove();
            name = value;
            display_value = 'all levels';
        }
        
        if (value == 'other') {
            $('input[name="template_other_name"]').show();
            $('input[name="template_other_name"]').focus();
            $('#template_name').text('');
        } else {
            $('#template_name').text(name.replace('_', ' '));
        }
        
        $('#template_level').text(display_value);
        $('input[name="template_type"]').val(value);
        $('input[name="location_id"]').val('');
        $('input[name="other_name"]').val('');
        $('input[name="template_other_name"]').val('');
    });
    
    $('#loading')
    .hide()  // hide it initially
    .ajaxStart(function() {
        $(this).show();
    })
    .ajaxStop(function() {
        $(this).hide();
    });
    
    $('#include_collection_code').change(function() {
        if($(this).attr('checked')){
            $('input[name="inc_col_code"]').val('true');
        } else {
            $('input[name="inc_col_code"]').val('false');
        } 
        if(collection_codes[collection_id] != undefined) {
            if(collection_codes[collection_id].length > 0) {
                var name = $('#template_name').text();
                if($('#include_collection_code').attr('checked')){
                    name += ' ' + collection_codes[collection_id];
                } else {
                    name = name.replace(' ' + collection_codes[collection_id], '');
                }    
                $('#template_name').text(name);
            }
        }
    });
    
    $('input[name="template_other_name"]').change(function() {
        var value = $(this).val();
        $('input[name="other_name"]').val(value);
    });
});

function get_libraries_list() {
    var owner = $('#owner').text();
    var html = '';
    $.ajax({
        async: true,
        url: 'Search',
        data: "owner=" + owner + "&level=library",
        dataType: "xml",
        success: function(xml){
            $('div.libraries ul').remove();
            $('div.collections ul').remove();
            $('div.shelves ul').remove();
            $('div.libraries').append('<ul>');
            libraries = new Array();
            $(xml).find('location').each(function(){
                if($(this).attr('type') == 'library') {
                    var id = $(this).find('locationid').first().text();
                    html = '<li class="clickable" id="' + id + '">';
                    libraries[id] = $(this).find('locationcode').first().text();
                    html += $(this).find('name').first().text().trim() + '</li>';
                    $('div.libraries ul').append(html);
                }
            });
            if($('div.libraries ul li').size() == 0) {
                html = '<li>No libraries</li>'
                $('div.libraries ul').append(html);
            }
        }
    });
}