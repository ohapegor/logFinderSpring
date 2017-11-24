function setColorScheme(r, g, b) {

    var style = {
        body: {
            bgR: r,
            bgG: g,
            bgB: b,
            fntR: 0,
            fntG: 0,
            fntB: 0
        },

        input: {
            bgR: getInputBgColor(r),
            bgG: getInputBgColor(g),
            bgB: getInputBgColor(b),
            fntR: 0,
            fntG: 0,
            fntB: 0
        }

    };

    setBodyFontColor(style);
    setInputFontColor(style);

    setBodyColors(style);
    setInputColors(style);

}

function setBodyColors(style) {
    document.body.style.backgroundColor = "rgb(" + style.body.bgR + "," + style.body.bgG + "," + style.body.bgB + ")";
    document.body.style.color = "rgb(" + style.body.fntR + "," + style.body.fntG + "," + style.body.fntB + ")";
}

function setInputColors(style) {
    var inputElems = document.getElementsByTagName('input');
    var index, item;
    for (index = 0; index <= inputElems.length; index++) {
        item = inputElems[index];
        item.style.backgroundColor = "rgb(" + style.input.bgR + "," + style.input.bgG + "," + style.input.bgB + ")";
        item.style.color = "rgb(" + style.input.fntR + "," + style.input.fntG + "," + style.input.fntB + ")";
    }
}


function getInputBgColor(color) {
    return Math.floor(color * 1.15 > 255 ? 255 : color * 1.15);

}

function setInputFontColor(style) {
    var inverseR = 255 - style.input.bgR;
    var inverseG = 255 - style.input.bgG;
    var inverseB = 255 - style.input.bgB;


    var modDelta = Math.sqrt((inverseR - style.input.bgR) * (inverseR - style.input.bgR) +
        (inverseG - style.input.bgG) * (inverseG - style.input.bgG) + (inverseB - style.input.bgB) * (inverseB - style.input.bgB));

    if (modDelta >= 35) {
        style.input.fntR = inverseR;
        style.input.fntG = inverseG;
        style.input.fntB = inverseB;
    } else {
        var modInput = Math.sqrt(style.input.bgR * style.input.bgR + style.input.bgG * style.input.bgG + style.input.bgB * style.input.bgB);
        if (modInput > modDelta) {
            style.input.fntR = 0;
            style.input.fntG = 0;
            style.input.fntB = 0;
        } else {
            style.input.fntR = 255;
            style.input.fntG = 255;
            style.input.fntB = 255;
        }
    }
}

function setBodyFontColor(style) {
    var inverseR = 255 - style.body.bgR;
    var inverseG = 255 - style.body.bgG;
    var inverseB = 255 - style.body.bgB;


    var modDelta = Math.sqrt((inverseR - style.body.bgR) * (inverseR - style.body.bgR) +
        (inverseG - style.body.bgG) * (inverseG - style.body.bgG) + (inverseB - style.body.bgB) * (inverseB - style.body.bgB));

    if (modDelta >= 35) {
        style.body.fntR = inverseR;
        style.body.fntG = inverseG;
        style.body.fntB = inverseB;
    } else {
        var modInput = Math.sqrt(style.body.bgR * style.body.bgR + style.body.bgG * style.body.bgG + style.body.bgB * style.body.bgB);
        if (modInput > modDelta) {
            style.body.fntR = 0;
            style.body.fntG = 0;
            style.body.fntB = 0;
        } else {
            style.body.fntR = 255;
            style.body.fntG = 255;
            style.body.fntB = 255;
        }
    }

}

