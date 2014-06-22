$(document).ready( function() {
    var editor = CodeMirror.fromTextArea(document.getElementById("contents"), {
        mode:  "javascript",
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