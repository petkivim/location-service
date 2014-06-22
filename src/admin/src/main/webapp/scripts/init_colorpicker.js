$(document).ready( function() {
    $('#select_color').ColorPicker({
        onSubmit: function(hsb, hex, rgb, el) {
            $(el).val(hex);
            $(el).ColorPickerHide();
        },
        onBeforeShow: function () {
            $(this).ColorPickerSetColor(this.value);
        },
        onChange: function (hsb, hex, rgb) {
            $('#select_color').val(hex);
        }
    })

    $('#colorSelector').ColorPicker({
        color: '#0000ff'
    });
});