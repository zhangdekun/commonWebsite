seajs.config({
    alias:{
        /* seajs */
        'seajs-combo':'seajs/seajs-combo/1.0.1/seajs-combo',
        'seajs-log':'seajs/seajs-log/1.0.1/seajs-log',
        'seajs-style':'seajs/seajs-style/1.0.2/seajs-style',
        'seajs-debug':'seajs/seajs-debug/1.1.1/seajs-debug',
        /* gallery */
        'daterangepicker':'gallery/daterangepicker/1.3.7/daterangepicker',
        'datetimepicker':'gallery/datetimepicker/2.0.0/datetimepicker',
        'dialog':'gallery/dialog/6.0.0/dialog',
        'easing':'gallery/easing/1.3.0/easing',
        'html5shiv':'gallery/html5shiv/3.7.0/html5shiv',
        'jquery':'gallery/jquery/1.11.1/jquery',
        'metadata':'gallery/metadata/1.0.0/metadata',
        'moment':'gallery/moment/2.5.1/moment',
        'respond':'gallery/respond/1.4.2/respond',
        'select2':'gallery/select2/3.5.0/select2',
        'st-grid':'gallery/st-grid/3.0.0/st-grid',
        'swfobject':'gallery/swfobject/2.3.0/swfobject',
        'ztree':'gallery/ztree/3.5.17/ztree',
        /* plugins */
        'ajax':'plugins/ajax/1.0.0/ajax',
        'parsejson':'plugins/parsejson/1.0.0/parsejson',
        'upxlsx':'plugins/upxlsx/1.0.0/upxlsx',
        'validate':'plugins/validate/1.0.0/validate',
        /* utils */
        'utils':'utils/utils',
        /* init */
        'init':'init/init'
    },
    map:[ [ /(^(?!.*(config|jquery|seajs-log|seajs-style|seajs-combo|seajs-debug)\.(css|js)).*)$/i, '$1?t=20140104'] ],
    preload:[ 'seajs-log', 'seajs-style', 'init'],
    debug: true,
    charset:'utf-8'
});
seajs.use('init');
