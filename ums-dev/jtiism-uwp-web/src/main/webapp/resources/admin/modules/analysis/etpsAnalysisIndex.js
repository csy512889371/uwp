var revennueCols = [
    {
        title: '企业名称（全称）',
        name: 'ENTINF001',
        width: 250,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return item.ENTINF001;
        }
    },
    {
        title: '统一社会信用代码',
        name: 'ENTINF015',
        width: 200,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return val;
        }
    },


    {
        title: '第一季度', align: 'center', cols: [
        {
            title: '税收(万元)',
            name: 'REPYOS011_1',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_1',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_1',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },


    {
        title: '第二季度', align: 'center', cols: [
        {
            title: '税收(万元)',
            name: 'REPYOS011_2',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_2',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_2',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },


    {
        title: '第三季度', align: 'center', cols: [
        {
            title: '税收(万元)',
            name: 'REPYOS011_3',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_3',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_3',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },
    {
        title: '第四季度', align: 'center', cols: [
        {
            title: '税收(万元)',
            name: 'REPYOS011_4',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_4',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_4',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },
];


var saleCols = [
    {
        title: '企业名称（全称）',
        name: 'ENTINF001',
        width: 250,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return item.ENTINF001;
        }
    },
    {
        title: '统一社会信用代码',
        name: 'ENTINF015',
        width: 200,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return val;
        }
    },
    {
        title: '第一季度', align: 'center', cols: [
        {
            title: '销售(万元)',
            name: 'REPYOS011_1',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_1',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_1',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },
    {
        title: '第二季度', align: 'center', cols: [
        {
            title: '销售(万元)',
            name: 'REPYOS011_2',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_2',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_2',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },


    {
        title: '第三季度', align: 'center', cols: [
        {
            title: '销售(万元)',
            name: 'REPYOS011_3',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_3',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_3',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },
    {
        title: '第四季度', align: 'center', cols: [
        {
            title: '销售(万元)',
            name: 'REPYOS011_4',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_4',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_4',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },
];


var profitCols = [
    {
        title: '企业名称（全称）',
        name: 'ENTINF001',
        width: 250,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return item.ENTINF001;
        }
    },
    {
        title: '统一社会信用代码',
        name: 'ENTINF015',
        width: 200,
        align: 'center',
        sortable: false,
        type: 'text',
        renderer: function (val, item) {
            return val;
        }
    },
    {
        title: '第一季度', align: 'center', cols: [
        {
            title: '利润(万元)',
            name: 'REPYOS011_1',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_1',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_1',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },


    {
        title: '第二季度', align: 'center', cols: [
        {
            title: '利润(万元)',
            name: 'REPYOS011_2',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_2',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_2',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },


    {
        title: '第三季度', align: 'center', cols: [
        {
            title: '利润(万元)',
            name: 'REPYOS011_3',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_3',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_3',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },
    {
        title: '第四季度', align: 'center', cols: [
        {
            title: '利润(万元)',
            name: 'REPYOS011_4',
            width: 80,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return val;
            }
        },
        {
            title: '同比增长',
            name: 'REPYOS012_4',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        },
        {
            title: '环比增长',
            name: 'REPYOS013_4',
            width: 95,
            align: 'center',
            sortable: false,
            type: 'text',
            renderer: function (val, item) {
                return parseFloat(val * 100, 2).toFixed(2).toString()+"%";
            }
        }
    ]
    },
];