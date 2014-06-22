$(document).ready( function() {

    var editor = CodeMirror.fromTextArea(document.getElementById("contents"), {
        mode: "text/html",
        tabMode: "indent",
        lineNumbers: true,
        lineWrapping: true,
        tabSize: 2,
        onCursorActivity: function() {
            editor.setLineClass(hlLine, null);
            hlLine = editor.setLineClass(editor.getCursor().line, "activeline");
        }
    });
    var hlLine = editor.setLineClass(0, "activeline");

    $(':input:visible:enabled:first').focus();
    
    $("#select_tags img").click( function( e ) {
        if(!$("#available_tags").is(':visible')) {
            $("#select_tags img[class='icon-plus']").attr('src', 'images/minus.png');
        } else {
            $("#select_tags img[class='icon-plus']").attr('src', 'images/plus.png');
        }
        $("#available_tags").toggle("slow");
    });

    $("#available_tags ul li").click( function( e ) {
        addLine(editor, $(this).attr('id'));
    });

    $("#select_includes img").click( function( e ) {
        if(!$("#available_includes").is(':visible')) {
            $("#select_includes img[class='icon-plus']").attr('src', 'images/minus.png');
        } else {
            $("#select_includes img[class='icon-plus']").attr('src', 'images/plus.png');
        }
        $("#available_includes").toggle("slow");
    });

    $("#available_includes ul li").click( function( e ) {
        var tag = "<!--$INCLUDE('" + $(this).attr('id') + "')-->";
        addLine(editor, tag);
    });
    
    $("#select_stylesheets img").click( function( e ) {
        if(!$("#available_stylesheets").is(':visible')) {
            $("#select_stylesheets img[class='icon-plus']").attr('src', 'images/minus.png');
        } else {
            $("#select_stylesheets img[class='icon-plus']").attr('src', 'images/plus.png');
        }
        $("#available_stylesheets").toggle("slow");
    });

    $("#available_stylesheets ul li").click( function( e ) {
        var owner = $('input[name="owner"]').val();
        var file = 'owners/' + owner + '/stylesheets/' + $(this).attr('id');
        if($(this).attr('id') == 'default') {
            file = 'owners/' + owner + '/style.css';
        }
        var tag = '<link rel="stylesheet" href="' + file + '" type="text/css" media="screen" />';
        addLine(editor, tag);
    });

    $("#select_scripts img").click( function( e ) {
        if(!$("#available_scripts").is(':visible')) {
            $("#select_scripts img[class='icon-plus']").attr('src', 'images/minus.png');
        } else {
            $("#select_scripts img[class='icon-plus']").attr('src', 'images/plus.png');
        }
        $("#available_scripts").toggle("slow");
    });

    $("#available_scripts ul li").click( function( e ) {
        var owner = $('input[name="owner"]').val();
        var file = 'owners/' + owner + '/scripts/' + $(this).attr('id');
        var tag = '<script type="text/javascript" src="' + file + '"></script>';
        addLine(editor, tag);
    });

    $("#select_scripts_sys img").click( function( e ) {
        if(!$("#available_scripts_sys").is(':visible')) {
            $("#select_scripts_sys img[class='icon-plus']").attr('src', 'images/minus.png');
        } else {
            $("#select_scripts_sys img[class='icon-plus']").attr('src', 'images/plus.png');
        }
        $("#available_scripts_sys").toggle("slow");
    });

    $("#available_scripts_sys ul li").click( function( e ) {
        var file = 'scripts/' + $(this).attr('id');
        var tag = '<script type="text/javascript" src="' + file + '"></script>';
        addLine(editor, tag);
    });

    $("#select_scripts_plugins img").click( function( e ) {
        if(!$("#available_scripts_plugins").is(':visible')) {
            $("#select_scripts_plugins img[class='icon-plus']").attr('src', 'images/minus.png');
        } else {
            $("#select_scripts_plugins img[class='icon-plus']").attr('src', 'images/plus.png');
        }
        $("#available_scripts_plugins").toggle("slow");
    });

    $("#available_scripts_plugins ul li").click( function( e ) {
        var file = 'scripts/plugins/' + $(this).attr('id');
        var tag = '<script type="text/javascript" src="' + file + '"></script>';
        addLine(editor, tag);
    });

    if($('#line').val() != '' && $('#ch').val() != '') {
        editor.setCursor(parseInt($('#line').val()), parseInt($('#ch').val()));
    }

    $('div.CodeMirror').css('max-width', parseInt($('div#main').css('width')) - 30);

    $(window).resize(function() {
        $('div.CodeMirror').css('max-width', parseInt($('div#main').css('width')) - 30);
    });

    $('form').submit(function() {
        $('#line').val(editor.getCursor().line);
        $('#ch').val(editor.getCursor().ch);
    });
});

function addLine(editor, tag) {
    var line = editor.getCursor().line;
    var position = editor.getCursor().ch;
    var oldData = editor.getLine(line);
    var newData = '';
    if(oldData.length == position) {
        newData = oldData + tag;
    } else if(position == 0) {
        newData = tag + oldData
    } else {
        newData = oldData.substring(0, position) + tag + oldData.substring(position);
    }
    editor.setLine(line, newData);
    editor.focus();
    editor.setCursor(line, newData.length);
}