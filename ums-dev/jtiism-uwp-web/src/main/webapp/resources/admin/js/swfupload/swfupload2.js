Object.notEmpty = function (o) {
    for (var k in o) return true;
    return false;
};
Object.isEmpty = function (o) {
    for (var k in o) return false;
    return true;
};
Object.clone = function () {
    var a = arguments[0], b = 1, c;
    while (c = arguments[b++]) {
        for (var i in c) {
            a[i] = c[i];
        }
    }
    return a;
};
Object.clonex = function () {
    var a = arguments[0], b = 1, c;
    while (c = arguments[b++]) for (var i in c) if (c.hasOwnProperty(i)) a[i] = c[i];
    return a;
};
Object.clonei = function () {
    var a = arguments[0], b = 1, c;
    while (c = arguments[b++]) for (var i in c) if (!a.hasOwnProperty(i)) a[i] = c[i];
    return a;
};
Object._extend = function (a, b, c) {
    if (typeof a == 'function') {
        if (Object.notEmpty(a.prototype)) {
            Object[c ? 'clone' : 'clonei'](a.prototype, b.prototype || b);
        } else {
            if (b.prototype) {
                var c = new Function;
                c.prototype = b.prototype;
                a.prototype = new c;
            } else {
                a.prototype = b;
            }
        }
    } else {
        Object[c ? 'clone' : 'clonei'](a, b.prototype || b);
    }
    return a;
};
Object.extend = function () {
    var b = 1, c;
    while (c = arguments[b++]) Object._extend(arguments[0], c);
    return arguments[0];
};
Object.extendz = function () {
    var b = 1, c;
    while (c = arguments[b++]) Object._extend(arguments[0], c, true);
    return arguments[0];
};
if (!window.Cfg) {
    var Cfg = {path: '', only_base: true};
}
if (!Cfg.img) {
    Cfg.img = Cfg.path + 'img/';
    Cfg.imgp = Cfg.img + 'p/';
    Cfg.imgm = Cfg.img + 'm/';
}
var Br = {};
var u = navigator.userAgent.toLowerCase();
Br.ie = navigator.appName === 'Microsoft Internet Explorer' ? true : false;
Br.ie11 = window.ActiveXObject !== undefined && document.documentMode > 10;
Br.ieVer = Br.ie ? parseFloat(u.substr(u.indexOf('msie') + 5)) : Br.ie11 ? 11 : false;
Br.gck = (/gecko/i.test(u) || u.indexOf(' applewebkit/') > -1);
Br.chm = /chrome/i.test(u);
Br.fox = /firefox/i.test(u);
Br.saf = /safari/i.test(u);
Br.opa = /opera/i.test(u);
Br.ie6 = Br.ie && Br.ieVer >= 6;
Br.ie7 = Br.ie && Br.ieVer >= 7;
Br.ie8 = Br.ie && Br.ieVer >= 8;
Br.ie9 = Br.ie && Br.ieVer >= 9;
Br.ie10 = Br.ie && (Br.ieVer >= 10 || document.documentMode > 9);
Br._ie6 = Br.ie6 && Br.ieVer < 7;
Br._ie7 = Br.ie7 && Br.ieVer < 8;
Br._ie8 = Br.ie8 && Br.ieVer < 9;
Br.ie_7 = Br.ie && (Br.ieVer < 8 || document.documentMode < 9);
Br.ie_8 = Br.ie && document.documentMode === 8;
Br.cmp = Br.gck || Br.opa || (Br.ie && document.documentMode > 7);
Br.w3c = Br.ie10 || Br.gck || Br.opa;
Br.iphone = u.indexOf('iphone') > 0;
Br.ipad = u.indexOf('ipad') > 0;
Br.android = u.indexOf('android') > 0;
Br.mobile = Br.ipad || Br.iphone || Br.android;
Br.mbios = Br.ipad || Br.iphone;
Br.css3 = Br.gck || Br.opa || (Br.ie9 && document.documentMode > 8);
Br.font = 6;
Br.auto = 0;
Br.scroll = 0;
Br.miniscroll = 0;
Br.local = window.location.protocol === 'file:';
Br.ajax_type = (function () {
    if (!Br.ie11 && window.XMLHttpRequest) {
        try {
            var o = new XMLHttpRequest();
            o.open('get', '/', true);
            return 1;
        } catch (e) {
        }
    }
    try {
        var a = 'MSXML2.XMLHTTP', o = new ActiveXObject(a), o = null;
        return a;
    } catch (e) {
    }
    try {
        var a = 'Microsoft.XMLHTTP', o = new ActiveXObject(a), o = null;
        return a;
    } catch (e) {
    }
    alert(' Cannot create XMLHTTP object! ');
})();
Br.ie6c = function () {
    return Br._ie6 && $.tags('select').length;
};

function Fn() {
};Fn._register_ = Fn.prototype._register_ = function (a, b) {
    if (b == null) {
        b = a;
        a = null;
    }
    if (a) {
        var c = this;
        if (a.indexOf('.') > -1) {
            var d = a.lastIndexOf('.');
            c = eval(' this.' + a.substr(0, d));
            a = a.substr(d + 1);
        }
        if (c[a] && Cfg.php) return alert(' Class "' + a + '" has registered! ');
        c[a] = b.Const;
        c = e = null;
        delete c;
        delete e;
    }
    if (!b.Const) b.Const = new Function;
    if (b.Extend) {
        for (var i = 0; i < b.Extend.length; i++) {
            Object._extend(b.Const, b.Extend[i], true);
        }
    }
    if (b.Xpro) {
        for (var i = 0; i < b.Xpro.length; i++) {
            if (b.Xpro[i].Prototype) Object._extend(b.Const, b.Xpro[i].Prototype, true);
        }
    }
    if (b.Prototype) {
        Object._extend(b.Const, b.Prototype, true);
    }
    b.Const.prototype.constructor = b.Const;
    b.Const.prototype._super = function (a) {
        return b.Extend[0].prototype[a].apply(this, js.argu(1));
    };
    if (b.Helper) Object.clone(b.Const, b.Helper);
    return b.Const;
};
Fn.reg = function (a, b) {
    if (b == null) {
        return Fn._register_(a);
    } else {
        var c = a.indexOf('.'), d = window;
        for (var i = 0, l = c.length - 1; i < l; i++) {
            if (!d[c[i]]) d = d[c[i]] = {};
        }
        return Fn.prototype._register_.call(d, a, b);
    }
};

function PL(a, b) {
    return Fn.prototype._register_.call(PL, a, b);
};Fn._register_('Event', {
    Const: function () {
    }, Helper: {
        _hashCodeCounter: 0, hashCode: function (o) {
            if (o._hashCode) return o._hashCode;
            return o._hashCode = '_' + (this._hashCodeCounter++).toString(32);
        }, _ev: null, setev: function (x, y, e) {
            this._ev = {x: x, y: y, e: e};
        }, getev: function () {
            return this._ev;
        }, win: function (w) {
            var a = w.frames, b;
            for (var i = 0; i < a.length; i++) {
                try {
                    if (a[i].event) return a[i];
                    if (b = arguments.callee(a[i])) return b;
                } catch (e) {
                }
            }
            return b;
        }, removeAllEvents: function (a) {
            if (a.df_listeners) {
                for (var k in a.df_listeners) a.removeEvent(k);
                delete a.df_listeners;
            }
            return a;
        }
    }, Prototype: {
        _disposed: false, addEvent: function (a, b, c, d, e, f) {
            if (this._disposed) return false;
            if (!this.df_listeners) this.df_listeners = {};
            if (!this.df_listeners[a]) this.df_listeners[a] = {};
            if (typeof b === 'string') {
                b = js.bind(b, c || this);
            }
            var g = Fn.Event.hashCode(b) + (c ? '-' + Fn.Event.hashCode(c) : '');
            this.df_listeners[a][g] = {_handler: b, _object: c, _args: d};
            if (e) this.df_listeners[a][g]._once = true;
            if (f) this.df_listeners[a][g]._name = f;
            return g;
        }, addEventOnce: function (a, b, c, d, e) {
            this.addEvent(a, b, c, d, true, e);
        }, fireEvent: function (a) {
            if (this._disposed || !this.df_listeners) return;
            if (this.df_listeners[a]) {
                var c = js.A, k, o, r;
                if (arguments.length > 1) c = c.concat(js.argu(1));
                for (k in this.df_listeners[a]) {
                    o = this.df_listeners[a][k];
                    if (o._object && o._object._disposed) {
                        this.removeEvent(a, o._handler, o._object);
                    } else {
                        if (o._once) delete this.df_listeners[a][k];
                        o._handler.apply(o._object || this, o._args ? js.A.concat.call(o._args, c) : c);
                        if (o._once) o._handler = o._object = null;
                    }
                }
            }
        }, removeEvent: function (a, b, c) {
            if (this._disposed || !this.df_listeners || !(a in this.df_listeners)) return;
            if (b) {
                var k = typeof(b) == 'string' ? b : (Fn.Event.hashCode(b) + (c ? '-' + Fn.Event.hashCode(c) : '')),
                    m = this.df_listeners[a][k];
                if (m) {
                    if (m._args) {
                        m._args = null;
                        delete m._args;
                    }
                    m._handler = m._object = null;
                    delete m._handler;
                    delete m._object;
                    delete this.df_listeners[a][k];
                }
            } else {
                for (var k in this.df_listeners[a]) {
                    var m = this.df_listeners[a][k];
                    if (m) {
                        if (m._args) {
                            m._args = null;
                            delete m._args;
                        }
                        m._handler = m._object = null;
                        delete m._handler;
                        delete m._object;
                        delete this.df_listeners[a][k];
                    }
                }
                delete this.df_listeners[a];
            }
        }, removeEventByName: function (a) {
            if (!this._disposed && this.df_listeners) {
                for (var i in this.df_listeners) {
                    for (var j in this.df_listeners[i]) {
                        if (this.df_listeners[i][j]._name == a) delete this.df_listeners[i][j];
                    }
                }
            }
        }, dispose: function () {
            if (this._disposed) return;
            if (this.df_listeners) {
                for (var k in this.df_listeners) this.removeEvent(k);
                delete this.df_listeners;
            }
            this._disposed = true;
        }
    }
});
Fn._register_('Node', {
    Const: function () {
    },
    Helper: {
        ELEMENT: 1,
        ATTRIBUTE: 2,
        TEXT: 3,
        CDATA_SECTION: 4,
        ENTITY_REFERENCE: 5,
        ENTITY: 6,
        PROCESSING_INSTRUCTION: 7,
        COMMENT: 8,
        DOCUMENT: 9,
        DOCUMENT_TYPE: 10,
        DOCUMENT_FRAGMENT: 11,
        NOTATION: 12,
        root: function (a, b) {
            a.rootNode = b.rootNode || b;
            if (a.length) {
                for (var i = 0; i < a.length; i++) arguments.callee(a[i], b);
            }
        },
        sort: function (a, b) {
            var i = a.length, c = Array.prototype.slice.call(a);
            c.sort(b);
            while (i--) (a[i] = c[i]).nodeIndex = i;
        }
    },
    Prototype: {
        _disposed: false, length: 0, nodeIndex: -1, addNode: function (a, b) {
            if (a.parentNode) {
                for (var i = a.nodeIndex, p = a.parentNode, l = p.length - 1; i < l; i++) (p[i] = p[i + 1]).nodeIndex = i;
                p.length--;
                delete p[i];
                delete p;
            }
            var l = this.length;
            if (b == null || b > l) b = l;
            a.parentNode = this;
            a.nodeIndex = b;
            Fn.Node.root(a, this);
            for (var k = l; k > b; k--) (this[k] = this[k - 1]).nodeIndex = k;
            this.length++;
            return this[b] = a;
        }, removeNode: function () {
            Fn.Node.prototype.dispose.call(this);
        }, popNode: function () {
            var i = this.nodeIndex, p = this.parentNode, l;
            if (p) {
                if (i > -1) {
                    for (l = p.length - 1; i < l; i++) (p[i] = p[i + 1]).nodeIndex = i;
                    p.length--;
                    delete p[i];
                }
                this.parentNode = this.rootNode = null;
            }
        }, dispose: function () {
            if (this._disposed) return;
            var i = this.length;
            while (i--) arguments.callee.call(this[i]);
            this.popNode();
            this._disposed = true;
        }
    }
});
Fn._register_('EventNode', {
    Const: function () {
    }, Extend: [Fn.Event, Fn.Node], Prototype: {
        removeNode: function () {
            Fn.EventNode.prototype.dispose.call(this);
        }, dispose: function () {
            if (this._disposed) return;
            Fn.Event.prototype.dispose.call(this);
            this._disposed = false;
            Fn.Node.prototype.dispose.call(this);
        }
    }
});
Fn._register_('Dom', {
    Const: function (a) {
        this.ww = a;
        this.ieBox = Br.ie && a.document.compatMode != 'CSS1Compat';
        this.ieCSS1 = Br.ie && !this.ieBox;
    },
    Helper: {
        _z: 9,
        div: document.createElement('div'),
        ins_pos: {
            '-2': 'beforebegin',
            '-1': 'afterbegin',
            '0': 'beforeend',
            '1': 'beforeend',
            '2': 'afterend',
            'beforebegin': 'previousSibling',
            'afterbegin': 'firstChild',
            'beforeend': 'lastChild',
            'afterend': 'nextSibling'
        },
        _attachResize: function (a, b) {
            var oEv = (Br._ie6 && a.ieCSS1 && a.ww != top) ? a.ww.document.documentElement : a.ww;
            oEv.attachEvent('onresize', b);
            a._resizer = b;
        },
        msXmldomName: function () {
            if (this._msXDN) return this._msXDN;
            var a = ['MSXML2.DOMDocument', 'Microsoft.XMLDOM', 'MSXML2.DOMDocument.3.0', 'MSXML2.DOMDocument.4.0', 'MSXML2.DOMDocument.5.0'];
            for (var i = 0, o; i < a.length; i++) {
                try {
                    o = new ActiveXObject(a[i]), o = null;
                    return this._msXDN = a[i];
                } catch (e) {
                }
            }
        },
        importNode: function (a, b) {
            var d = a.ownerDocument;
            if (Br.w3c && b.ownerDocument != d) {
                if (Br.ie10) {
                    var doc2 = d.cloneNode(false);
                    doc2.loadXML(b.xml);
                    b = doc2.documentElement;
                }
                if ('importNode' in d) b = d.importNode(b, true);
            }
            return b;
        },
        _msXDN: '',
        props: {
            'class': 'className',
            'for': 'htmlFor',
            'colspan': 'colSpan',
            'rowspan': 'rowSpan',
            'accesskey': 'accessKey',
            'tabindex': 'tabIndex',
            'maxlength': 'maxLength',
            'readonly': 'readOnly',
            'value': 'value',
            'disabled': 'disabled',
            'checked': 'checked',
            'multiple': 'multiple'
        }
    },
    Prototype: {
        query: function (a, b) {
            return jQuery.find(a, b)
        }, get: function (a, b) {
            return jQuery.find(a, b)[0]
        }, tags: function (a, b) {
            if (!b || !b.nodeType) b = $.canvas(); else if (b.nodeType == 9) b = b.documentElement || b.getElementsByTagName('html')[0];
            return js.map(b.getElementsByTagName(a));
        }, tagis: function (a, b) {
            return a.tagName.toUpperCase() == b.toUpperCase() || b == '*';
        }, ns: function (a, b) {
            var c = this.ww.document.getElementsByName(a), d = [];
            if (!b) b = this.db();
            for (var i = 0, e; i < c.length; i++) {
                e = c[i];
                if (b.contains(e)) d.push(e);
            }
            a = b = c = e = null;
            return d;
        }, attr: function (a, b, c) {
            var t = js.type(a) == 'html';
            if (typeof b == 'string') {
                var l = arguments.length;
                if (l >= 3) {
                    for (var i = 1, d, e, f; i < l; i += 2) {
                        d = arguments[i], e = arguments[i + 1];
                        if (t) {
                            $.e_prop(a, d, e);
                        } else {
                            if ('setAttribute' in a) a.setAttribute(d, e); else a[d] = e;
                        }
                    }
                    return a;
                }
                return t ? $.e_prop(a, b) : ('getAttribute' in a ? a.getAttribute(b) : a[b]);
            } else {
                for (var i in b) {
                    if (t) {
                        $.e_prop(a, i, b[i]);
                    } else {
                        if ('setAttribute' in a) a.setAttribute(i, b[i]); else a[i] = b[i];
                    }
                }
            }
            return a;
        }, removeAttr: function (a, b) {
            a.removeAttribute(b)
        }, _css_camelize: (function (a) {
            var b = {'float': Br.ie ? 'styleFloat' : 'cssFloat'};
            return function (s) {
                return b[s] || s.replace(/-./g, function (c) {
                    return c.charAt(1).toUpperCase()
                })
            }
        })(), cur_css: (function () {
            return Br.ieVer ? function (a, b) {
                return a.currentStyle[$._css_camelize(b)]
            } : function (a, b) {
                return a.ownerDocument.defaultView.getComputedStyle(a, null).getPropertyValue(b)
            }
        })(), css: function (a, v) {
            if (typeof a === 'string') a = this.ww.$(a);
            if (!a || !v) return a;
            var ar;
            if (arguments.length > 2) {
                ar = js.argu(1);
            } else if (typeof v === 'string') {
                if (v.indexOf(':') < 0) {
                    return $.cur_css(a, v)
                } else ar = v.split(/:(?!\/)|;/);
            } else {
                ar = [];
                for (var i in v) ar.push(i, v[i]);
            }
            for (var i = 0; i < ar.length; i += 2) {
                var b = ar[i], d = ar[i + 1], c;
                if (!b) continue;
                if (b.charAt(0) === '+') {
                    if (d) {
                        b = $._css_camelize(b);
                        c = js.m(a.style[b]);
                        d = js.m(d);
                        if ((b === 'width' || b === 'height')) {
                            if (a.style[b] === '') c = js.m($[b === 'width' ? 'boxwd' : 'boxht'](a, a['offset' + js.s_up(b)]));
                            if (c < -d) c = -d;
                        }
                        a.style[b] = (c + d) + 'px';
                    }
                } else {
                    b = $._css_camelize(b);
                    if (b === 'width' || b === 'height') d = Math.max(js.m(d), 0);
                    if (typeof d === 'number' && b !== 'zIndex') d += 'px';
                    a.style[b] = d;
                }
            }
            return a;
        }, inhercss: function (a, b, c, d) {
            var e;
            while (!(e && e != c) && a && a.nodeType != 9) {
                e = a.currentStyle[b];
                a = a.parentNode;
            }
            if (d && e) d.style[b] = e;
            return e;
        }, css_import_link: function (a, b, w, c) {
            if (typeof a === 'object') {
                a = js.unique(a);
                var d = a.length, f = function () {
                    if (--d == 0 && b) b.call(c)
                };
                for (var i = 0; i < a.length; i++) this._css_import_link(a[i], f, w, c);
            } else this._css_import_link.apply(this, arguments);
        }, _css_create_link: function (a, b, w, c) {
            var l = (w || this.ww).document.createElement("link");
            l.setAttribute("rel", "stylesheet");
            l.setAttribute("type", "text/css");
            if (b) {
                l.onreadystatechange = function () {
                    if (this.readyState === 'complete' && !this.complete) {
                        this.complete = true;
                        b.call(c)
                    }
                }
            }
            l.setAttribute("href", a);
            l.id = c;
            return l;
        }, _css_import_link: function (a, b, w, c) {
            if (!c) c = a;
            if (w ? w.document.getElementById(c) : this(c)) {
                if (b) b();
                return;
            }
            var l = this._css_create_link(a, b, w, c);
            this.tags('head', (w || this.ww).document)[0].appendChild(l);
            return l;
        }, css_import_style: function (a, b) {
            if (!b || !this(b)) {
                var c = document.createElement("style");
                c.setAttribute("type", "text/css");
                if (c.styleSheet) {
                    c.styleSheet.cssText = a;
                } else {
                    c.appendChild(document.createTextNode(a));
                }
                if (b) c.id = b;
                this.tags('head')[0].appendChild(c);
            }
        }, class_any: function (a) {
            if (typeof a == 'object') a = a.className;
            for (var i = 1; i < arguments.length; i++) if (js.ids_inc(a, arguments[i], ' ')) return true;
            return false;
        }, class_add: function (a, b, c) {
            a.className = js[c ? 'ids_del' : 'ids_add'](a.className, b, ' ')
        }, class_del: function (a, b) {
            a.className = js.ids_del(a.className, b, ' ')
        }, class_replace: function (a, b, c) {
            a.className = js.ids_add(js.ids_del(a.className, b, ' '), c, ' ')
        }, class_toggle: function (a, b, c) {
            if (typeof c === 'string') {
                if ($.class_any(a, b)) $.class_replace(a, b, c); else $.class_replace(a, c, b);
            } else {
                c = c == null ? $.class_any(a, b) : !c;
                $.class_add(a, b, c);
            }
        }, class_set: function (a, b) {
            a.className = b
        }, html: function (a, b, c) {
            if (a) {
                var d = typeof b === 'object';
                if (c == null) {
                    if (b == null) return a.innerHTML;
                    if (d) $.empty(a).appendChild(b); else $._html(a, b);
                    return a.lastChild;
                } else if (typeof c === 'string') {
                    c = c.toLowerCase();
                    if (d) $._append(a, b, c); else $._html(a, b, c);
                    return a[Fn.Dom.ins_pos[c]];
                } else {
                    var e = a.children[c];
                    if (e) {
                        if (d) a.insertBefore(b, e); else $._html(e, b, 'beforebegin');
                        return e.previousSibling;
                    } else {
                        if (d) a.appendChild(b); else $._html(a, b, 'beforeend');
                        return a.lastChild;
                    }
                }
            }
        }, _html: function (a, b, c) {
            try {
                if (c) a.insertAdjacentHTML(c, b); else a.innerHTML = b;
            } catch (ex) {
                if (c == null) $.empty(a);
                var e = $.div(b), f = c.indexOf('after') == 0;
                while (e.firstChild) $._append(a, e[f ? 'lastChild' : 'firstChild'], c);
            }
        }, _append: function (a, b, c) {
            if (c == 'beforebegin') a.parentNode.insertBefore(b, a); else if (c == 'afterbegin') a.insertBefore(b, a.firstChild); else if (c == 'afterend') a.parentNode.insertBefore(b, a.nextSibling); else a.appendChild(b);
        }, xml: function (a, b, c) {
            if (a) {
                if (typeof b == 'string') b = $.x_doc(b);
                if (Br.w3c) b = Fn.Dom.importNode(a, b);
                if (c == null) {
                    $.empty(a).appendChild(b);
                } else if (typeof c == 'string') {
                    c = c.toLowerCase();
                    if (c == 'beforebegin') a.parentNode.insertBefore(b, a); else if (c == 'afterend') a.parentNode.insertBefore(b, a.nextSibling); else if (c == 'afterbegin') a.insertBefore(b, a.firstChild); else a.appendChild(b);
                } else {
                    var e = $.x(a, '*[' + (c + 1) + ']');
                    if (e) a.insertBefore(b, e); else a.appendChild(b);
                }
                return b;
            }
        }, _hox: function (a) {
            return a.nodeType == 1 && !('insertAdjacentHTML' in a) ? 'xml' : 'html';
        }, append: function (a, b, c) {
            return this[$._hox(a)](a, b, c == null ? 'beforeend' : c);
        }, prepend: function (a, b, c) {
            return this[$._hox(a)](a, b, c == null ? 'afterbegin' : c);
        }, before: function (a, b, c) {
            return this[$._hox(a)](a, b, c == null ? 'beforebegin' : c);
        }, after: function (a, b, c) {
            return this[$._hox(a)](a, b, c == null ? 'afterend' : c);
        }, rm: function (a) {
            if (typeof a === 'string') a = this(a);
            if (a) {
                if (Br.ie) a.removeNode(true); else if (a.parentNode) a.parentNode.removeChild(a);
            }
        }, empty: function (a) {
            for (var i = a.childNodes.length - 1; i > -1; i--) a.removeChild(a.childNodes[i]);
            return a;
        }, win: function (a) {
            return a ? this.dc(a)[Br.ie ? 'parentWindow' : 'defaultView'] : this.ww;
        }, dc: function (a) {
            return a ? (a.ownerDocument || o.document) : this.ww.document;
        }, db: function (a, b) {
            return a ? this.append((b || this.ww).document.body, a) : this.ww.document.body;
        }, canvas: function (a) {
            return document.documentElement || document.body;
        }, canrec: function (a) {
            return {
                width: this.canvas().clientWidth || document.body.clientWidth,
                height: this.canvas().clientHeight || document.clientHeight
            }
        }, hmin: function (a) {
            var b = 0, c = a.currentStyle,
                d = [c.borderLeftWidth, c.borderRightWidth, c.paddingLeft, c.paddingRight, c.marginLeft, c.marginRight];
            for (var i = 0, n; i < d.length; i++) {
                n = parseFloat(d[i]);
                if (!isNaN(n)) b += n;
            }
            return b;
        }, vmin: function (a) {
            var b = 0, c = a.currentStyle,
                d = [c.borderTopWidth, c.borderBottomWidth, c.paddingTop, c.paddingBottom, c.marginTop];
            for (var i = 0, n; i < d.length; i++) {
                n = parseFloat(d[i]);
                if (!isNaN(n)) b += n;
            }
            return b;
        }, pel: function (a, b, c) {
            if (!a) return;
            if (typeof a.$ === 'function') a = a.$();
            if (!a.parentNode && a.length) a = a[0];
            while (a && a.tagName !== b) a = a.parentNode;
            if (c && a) {
                var d = a.parentNode.parentNode;
                if (b === 'TD') d = d.parentNode;
                if (d != c) a = this.pel(d.parentNode, b, c);
            }
            return a;
        }, pcls: function (a, b) {
            while (a && a.nodeType == 1) {
                if ($.class_any(a, b)) return a;
                a = a.parentNode;
            }
        }, div: function (a, b) {
            var c = (b ? (b.nodeType === 9 ? b : b.ownerDocument) : this.ww.document).createElement('div');
            c.innerHTML = a;
            return c;
        }, bcr: function (o, e) {
            if (e) return this.offrec(o, e);
            var n = o.nodeType, a = n ? o.getBoundingClientRect() : o.length === 2 ? {
                    left: o[0],
                    right: o[0],
                    top: o[1],
                    bottom: o[1]
                } : o, w = this.ww, v = $.canvas(w), b = v.clientLeft * (w == top ? 1 : 0), c = v.scrollTop,
                d = v.scrollLeft;
            if (a.left === 0 && a.right === 0 && n) {
                a = o.getBoundingClientRect();
            }
            var r = {left: a.left - b + d, top: a.top - b + c, right: a.right - b + d, bottom: a.bottom - b + c};
            r.width = r.right - r.left;
            r.height = r.bottom - r.top;
            return r;
        }, offrec: function (a, p) {
            var b = a.offsetLeft, c = a.offsetTop, d = a.offsetWidth, e = a.offsetHeight;
            if (p) {
                if (p.nodeType != 1) p = $.db();
                while ((a = a.offsetParent) && p.contains(a) && p != a) {
                    b += a.offsetLeft;
                    c += a.offsetTop;
                }
            }
            return {left: b, top: c, right: b + d, bottom: c + e, width: d, height: e};
        }, snaprec: function (a, b, c, d, r) {
            return this.snapbcr(a, b, this.bcr(c), d, r);
        }, snaptype: {h: '21,12,34,43', v: '41,14,32,23'}, snapbcr: (function () {
            var pos_num = {'14': 't', '23': 't', '21': 'r', '34': 'r', '12': 'l', '43': 'l', '41': 'b', '32': 'b'},
                pos_chr = {'tb': 't', 'rl': 'r', 'lr': 'l', 'bt': 'b'};
            return function (a, b, c, d, r) {
                var t = [], l = [], f, g, h, e = this.canvas(), ew = e.clientWidth, eh = e.clientHeight, k = -1;
                if (r) {
                    e = $.bcr(r);
                    ew = e.width, eh = e.height;
                }
                if (d) {
                    if (d === 'h' || d === 'v') d = $.snaptype[d];
                    f = String(d).split(',');
                } else f = ['41', '32', '14', '23', '21', '34', '12', '43', '11'];
                if (/[1-4]/.test(f[0])) {
                    for (var i = 0, o, p, m, n = 0; i < f.length; i++) {
                        g = f[i].charAt(0);
                        h = f[i].charAt(1);
                        t.push((g === '1' || g === '2' ? c.top : c.bottom) - (h === '3' || h === '4' ? b : 0));
                        l.push((g === '1' || g === '4' ? c.left : c.right) - (h === '2' || h === '3' ? a : 0));
                        o = t[i] >= 0 && t[i] + b <= eh;
                        p = l[i] + a <= ew && l[i] >= 0;
                        if (o && p) {
                            k = i;
                            break;
                        } else if (o || p) {
                            if (d) {
                                m = d === $.snaptype.v ? (g === '1' || g === '2' ? -t[i] : t[i] + b - eh) : (g === '1' || g === '4' ? -l[i] : l[i] + a - ew);
                                if (!n || n > m) {
                                    n = m;
                                    k = i;
                                }
                            } else if (k < 0) k = i;
                        }
                    }
                } else {
                    for (var i = 0, o, p, m, n = 0; i < f.length; i++) {
                        g = f[i].charAt(0);
                        h = f[i].charAt(1);
                        t.push(Math.floor((g === 'r' || g === 'l' ? (c.top + c.bottom) / 2 : (g === 't' ? c.top : c.bottom)) - (h === 'r' || h === 'l' ? b / 2 : (h === 'b' ? b : 0))));
                        l.push(Math.floor((g === 't' || g === 'b' ? (c.left + c.right) / 2 : (g === 'r' ? c.right : c.left)) - (h === 't' || h === 'b' ? a / 2 : (h === 'r' ? a : 0))));
                        o = t[i] >= 0 && t[i] + b <= eh;
                        p = l[i] + a <= ew && l[i] >= 0;
                        if (o && p) {
                            k = i;
                            break;
                        } else if (o || p) {
                            if (d) {
                                m = d == $.snaptype.v ? (g === '1' || g === '2' ? -t[i] : t[i] + b - eh) : (g === '1' || g === '4' ? -l[i] : l[i] + a - ew);
                                if (!n || n > m) {
                                    n = m;
                                    k = i;
                                }
                            } else if (k < 0) k = i;
                        }
                    }
                }
                if (k < 0) k = 0;
                var p = f[k], q = {left: l[k], top: t[k], right: l[k] + a, bottom: t[k] + b, type: p};
                if (q.pos = pos_chr[p]) {
                    if (q.right > ew) {
                        q.left -= q.right - ew;
                        q.right -= q.right - ew;
                    }
                    if (q.left < 0) q.left = 0;
                } else q.pos = pos_num[p];
                return q
            }
        })(), attachResize: function (a, b, c) {
            try {
                var ce = Br.ie7 && this.ww != top ? this.ww.frameElement : this.canvas(), cw = ce.clientWidth,
                    ch = ce.clientHeight;
                Fn.Dom._attachResize(this, function () {
                    var w = ce.clientWidth, h = ce.clientHeight;
                    if (w <= 0 || h <= 0) return;
                    if (b != null) w = Math.max(b, w);
                    if (c != null) h = Math.max(c, h);
                    if (w != cw || h != ch) {
                        a(w, h, w - cw, h - ch);
                        cw = w;
                        ch = h;
                    }
                });
            } catch (e) {
            }
        }, fireResize: function () {
            if (Br.ie7 && this.ieCSS1 && this._resizer) this._resizer();
        }, e_prop: function (a, b, c) {
            var f = Fn.Dom.props[b];
            if (c != null) {
                if (f || !('setAttribute' in a)) a[f] = c; else a.setAttribute(b, c);
            } else {
                var g = f || !('getAttribute' in a) ? a[f] : a.getAttribute(b);
                if (g && b == 'class') g = js.s_trim(g);
                return g;
            }
        }, e_moveup: function (a, b, c, d) {
            if (event.type !== 'mousemove') event.returnValue = false;
            var o = document, e;
            if (d) {
                var f = event.clientX, g = event.clientY, h = false, k = b;
                b = function (ev) {
                    if (h || Math.abs(event.clientX - f) > 4 || Math.abs(event.clientY - g) > 4) {
                        h = true;
                        k(ev);
                    }
                }
            }
            o.attachEvent('onmousemove', b);
            o.attachEvent('onmouseup', e = function (ev) {
                if (c) c(ev);
                o.detachEvent('onmousemove', b);
                o.detachEvent('onmouseup', e);
            });
        }, e_touch: function (a, b, c, d) {
            a.addEventListener('touchstart', b, false);
            a.addEventListener('touchmove', c, false);
            a.addEventListener('touchend', d, false);
        }, e_stop: function (a) {
            if (a || (a = this.ww.event)) {
                if (Br.ie) {
                    a.cancelBubble = true;
                    a.returnValue = false;
                } else {
                    a.stopPropagation();
                    a.preventDefault();
                }
            }
        }, e_cancel: function (a) {
            if (a || (a = this.ww.event)) {
                Br.ie ? a.cancelBubble = true : a.stopPropagation()
            }
        }, _bindHdl: {
            _a: {}, _bind: function (a, b, c) {
                var e = ('getAttribute' in c) ? c.getAttribute('dfish') : c.dfish, f = this._a[e];
                if (!e) {
                    e = $.eid(9);
                    ('setAttribute' in c) ? c.setAttribute('dfish', e) : c.dfish = e;
                    f = this._a[e] = new Fn.Event();
                    f.fes = {};
                }
                if (!b) {
                    f.fireEvent(a, {type: a, srcElement: c});
                    if (('on' + e) in c && typeof c['on' + e] == 'function') c['on' + e]();
                } else {
                    if (!f.fes[a]) {
                        f.fes[a] = function (g) {
                            f.fireEvent(a, g)
                        };
                        if (Br.ie) c.attachEvent('on' + a, f.fes[a]); else c.addEventListener(a, f.fes[a], false);
                    }
                    f.addEvent(a, b, c);
                }
            }, _unbind: function (a, b, c) {
                var e = c.nodeType == 1 ? c.getAttribute('dfish') : c.dfish, f = this._a[e];
                if (!e || !f || !f.fes[a]) return;
                f.removeEvent(a, b, c);
                if (!f.df_listeners[a]) {
                    if (Br.ie) c.detachEvent('on' + a, f.fes[a]); else c.removeEventListener(a, f.fes[a], false);
                    delete f.fes[a];
                }
            }
        }, e_attach: function (a, b, c, d) {
            if (Br.ie) {
                a.attachEvent('on' + b, c);
                if (d) a.setCapture();
            } else {
                (d ? document : a).addEventListener(b, c, false);
            }
        }, e_detach: function (a, b, c, d) {
            if (Br.ie) {
                a.detachEvent('on' + b, c);
                if (d) a.releaseCapture();
            } else {
                (d ? document : a).removeEventListener(b, c, false);
            }
        }, e_add: function (a, b, c, d) {
            var g = a.nodeType;
            for (var i = 0, b = b.split(' '), e; i < b.length; i++) {
                if (e = b[i]) {
                    if (g) $._bindHdl._bind(e, c, a); else if ('attachEvent' in a) a.attachEvent('on' + e.replace(/^on/, ''), c);
                }
            }
        }, e_remove: function (a, b, c) {
            var g = a.nodeType;
            for (var i = 0, b = b.split(' '), e; i < b.length; i++) {
                if (e = b[i]) {
                    if (g) $._bindHdl._unbind(e, c, a); else if ('detachEvent' in a) a.detachEvent('on' + e.replace(/^on/, ''), c);
                }
            }
        }, boxwd: function (o, w) {
            w = w == null ? o.offsetWidth : parseFloat(w);
            var c = o.currentStyle, ar = [c.borderLeftWidth, c.borderRightWidth, c.paddingLeft, c.paddingRight];
            for (var i = 0, n; i < 4; i++) {
                n = parseInt(ar[i]);
                if (!isNaN(n)) w = w - n < 0 ? 0 : (w - n);
            }
            if (o.colSpan && this.ieCSS1) w -= o.colSpan - 1;
            return w;
        }, boxht: function (o, h) {
            h = h == null ? o.offsetHeight : parseFloat(h);
            var c = o.currentStyle, ar = [c.borderTopWidth, c.borderBottomWidth, c.paddingTop, c.paddingBottom];
            for (var i = 0, n; i < 4; i++) {
                n = parseInt(ar[i]);
                if (!isNaN(n)) h = h - n < 0 ? 0 : (h - n);
            }
            return h;
        }, coltb: function (o, w, b) {
            var c = o.getAttribute('ikcols');
            if (w < 0) return o;
            if (!w) w = js.m(o.style.width || o.width);
            if (c) {
                var g = js.find(o.children, 'v.tagName=="COLGROUP"');
                if (false && o.cellSpacing) w -= o.cellSpacing * (((g && g.children) || o.rows[0].cells).length + 1);
                var ms = js.m_scalegrid(w, c);
                if (g) {
                    js.each(o.firstChild.children, function (v, i) {
                        v.style.width = Math.abs(ms[i] - js.m(v.getAttribute('ikmin')) - js.m(v.getAttribute('minus'))) + 'px';
                    });
                } else {
                    var r = js.select(o.rows, 'v.cells.length==' + ms.length);
                    if (r.length) {
                        for (var i = 0; i < r.length; i++) {
                            for (var j = 0, t = r[i].cells; j < t.length; j++) $.css(t[j], 'width', $.boxwd(t[j], ms[j]));
                        }
                        r = js.select(o.rows, 'v.cells.length<' + ms.length);
                        for (var i = 0; i < r.length; i++) {
                            for (var j = 0, t = r[i].cells; j < t.length; j++) $.css(t[j], 'width', 'auto');
                        }
                    } else {
                        r = o.rows[0].cells;
                        for (var n = j = 0; j < r.length; j++) {
                            var tc = r[j].colSpan, w = 0;
                            for (var k = 0; k < tc; k++) w += ms[n + k] || 0;
                            n = k;
                            r[j].style.width = $.boxwd(r[j], w) + 'px';
                        }
                    }
                }
            }
            o.style.width = Math.max(w, 0) + 'px';
            return o;
        }, view: function (a, b, m) {
            var c = this.bcr(a), d = this.bcr(b), t, l;
            if (d.top < c.top) t = d.top - c.top; else if (d.bottom > c.bottom) t = d.bottom - c.bottom;
            if (d.left < c.left) l = d.left - c.left; else if (d.right > c.right) l = d.right - c.right + Math.min(d.width, c.width);
            if (m) {
                var f = a.scrollTop, g = a.scrollLeft;
                js.fps_timer(function (p) {
                    if (t) a.scrollTop = f + t * p;
                    if (l) a.scrollLeft = g + t * p;
                });
            } else {
                if (t) a.scrollTop += t;
                if (l) a.scrollLeft += l;
            }
        }, imgfix: function (o, mxw, mxh) {
            if (!o || !mxw || !mxh) return;
            o.style.maxWidth = mxw + 'px';
            o.style.maxHeight = mxh + 'px';
            if (Br._ie6) {
                var w = o.width, h = o.height;
                if (!w && !h) {
                    var t = new Image();
                    t.src = o.src;
                    w = t.width;
                    h = t.height;
                }
                if (w && h) {
                    if (Br._ie6) {
                        var b1 = w / h, b2 = mxw / mxh;
                        if (b1 > b2 && w > mxw) {
                            w = o.width = mxw;
                            h = o.height = mxw / b1;
                        } else if (b1 <= b2 && h > mxh) {
                            o.height = mxh;
                            o.width = w * (o.height / h);
                        }
                    }
                }
            }
            return true;
        }, toggleAttr: function (a, b, c, d, y) {
            var e = y ? a.getAttribute(b) : eval('a.' + b), f = '_' + b.replace(/\./g, '_');
            if (a.getAttribute(f) == null) a.setAttribute(f, e);
            var g = a.getAttribute(f);
            if (y) a.setAttribute(b, (d != null ? (d == true ? c : g) : (e == g ? c : g))); else eval('a.' + b + '="' + (d != null ? (d == true ? c : g) : (e == g ? c : g)) + '"');
            return d != null ? d : (e == g);
        }, toggle: function (a, b) {
            if ($.class_any(a, 'none')) {
                $.class_add(a, 'none', b);
            } else {
                var e = a.currentStyle.display, f = a.getAttribute('df_toggle');
                if (f == null) a.setAttribute('df_toggle', f = e);
                if (typeof b === 'string') {
                    a.style.display = b;
                } else a.style.display = (typeof b === 'boolean' ? b : (e === 'none')) ? (f === 'none' ? '' : f) : 'none';
            }
        }, htm: {
            iframe: function (a, b, c, d, e, f) {
                if (!d) d = 'ikframe-' + js.uid();
                return '<iframe style="width:' + js.m(a) + 'px;' + (b == null ? '' : 'height:' + js.m(b) + 'px;') + (e || '') + ';" src="' + (c || 'about:blank') + '" marginwidth=0 marginheight=0 frameborder=0 id=' + d + ' name=' + d + (f ? ' ' + f : '') + ' allowtransparency></iframe>';
            },
            hiframe: function (a, b) {
                if (!a) a = 'ikframe-' + js.uid();
                return this.iframe(1000, 1, b, a, 'position:absolute;top:-1px;left:-1px;');
            },
            ie6fr: function (a, b, c, d, e, f) {
                if (!d) d = 'ikframe-' + js.uid();
                return '<iframe style="width:' + js.m(a) + 'px;height:' + js.m(b) + 'px;' + (e || '') + ';" src="' + (c || 'about:blank') + '" marginwidth=0 marginheight=0 frameborder=0 id=' + d + ' name=' + d + (f ? ' ' + f : '') + '></iframe>';
            },
            cmptag: function (a) {
                return '<!doctype html><html xmlns:v="urn:schemas-microsoft-com:vml"><head><meta charset="utf-8">' + (a || '') + '</head>';
            },
            vmltag: function (a, b) {
                return '<!doctype html><html xmlns:v="urn:schemas-microsoft-com:vml"><head><base target="_blank"/><meta charset="utf-8"><STYLE>BODY,TABLE{font-size:12px;}BODY,TABLE,P,DIV{line-height:135%;font-family:simsun,Verdana,Arial;}FONT,SPAN{line-height:135%;}P,FORM{margin:0;}v\\:*{behavior:url(#default#VML);}' + (a || '') + '</STYLE>' + (b || '') + '</head>';
            },
            jstag: function () {
                return '<script>function $(a){return document.getElementById(a)}</script>';
            },
            png: function (a, b, c, d, e) {
                return Br._ie6 ? ('<span style="display:inline-block;width:' + a + 'px;height:' + b + 'px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image,src=' + c + ');' + (d || '') + '" ' + (e || '') + '></span>') : ('<img src="' + c + '" width=' + a + ' height=' + b + (d ? ' style="' + d + '"' : '') + (e ? ' ' + e : '') + '>');
            },
            flash: function (iw, ih, sSrc) {
                return '<object width="' + iw + '" height="' + ih + '"><param name="movie" value="' + sSrc + '"></param><param name="wmode" value="transparent"></param><embed src="' + sSrc + '" type="application/x-shockwave-flash" width="' + iw + '" height="' + ih + '"></embed></object>';
            },
            media: function (sType, sSrc, bAutoStart) {
                var iAuto = bAutoStart ? 1 : 0;
                switch (sType) {
                    case 'rm':
                    case 'rmvb':
                    case 'ram':
                    case 'ra':
                        return '<OBJECT classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" height=309 width=352 VIEWASTEXT><param name="_ExtentX" value="5503"><param name="_ExtentY" value="1588"><param name="AUTOSTART" value="' + iAuto + '"><param name="SHUFFLE" value="0"><param name="PREFETCH" value="0"><param name="NOLABELS" value="0"><param name="SRC" value="' + sSrc + '"><param name="CONTROLS" value="Imagewindow,StatusBar,ControlPanel"><param name="CONSOLE" value="RAPLAYER"><param name="LOOP" value="0"><param name="NUMLOOP" value="0"><param name="CENTER" value="0"><param name="MAINTAINASPECT" value="0"><param name="BACKGROUNDCOLOR" value="#000000"></OBJECT>';
                        break;
                    default:
                        return '<object width=352 height=309 classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=6,4,5,715" standby="WMP" type="application/x-oleobject" align="center" hspace="5"><param name="AutoRewind" value=1><param name="FileName" value="' + sSrc + '"><param name="ShowStatusBar" value="1"><param name="AutoStart" value="' + iAuto + '"><param name="Volume" value="50%"><param name="AnimationAtStart" value="0"><param name="TransparentAtStart" value="0"><param name="AllowChangeDisplaySize" value="0"><param name="AllowScan" value="0"><param name="EnableContextMenu" value="0"><param name="ClickToPlay" value="0"><param name="EnablePositionControls" value="1"><param name="EnableFullScreenControls" value="1"><param name="EnableTracker" value="1"><param name="ShowTracker" value="1"></object>';
                        break;
                }
            },
            arw: function (a, b) {
                return '<cite class="f-arw-' + (a || 'b1') + '"' + (b ? (b.style ? ' style="' + b.style + '"' : '') + (b.id ? ' id="' + b.id + '"' : '') : '') + '>' + (Br._ie6 ? '<em class=f-arw><i>' + Loc.f_arw + '</i></em>' : '') + '</cite>';
            },
            prong: (function () {
                var n = {t: 'bottom', r: 'left', b: 'top', l: 'right'};
                return function (a, b, c, d) {
                    a = a.charAt(0);
                    return '<div class=d-prong-' + a + (b ? ' style="' + b + '"' : '') + '>' + $.htm.arw(a + 4, c && {style: 'color:' + c + ';border-' + n[a] + '-color:' + c}) + $.htm.arw(a + 3, d && {style: 'color:' + d + ';border-' + n[a] + '-color:' + d}) + '</div>'
                }
            })(),
            dot: 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==',
            ic: function (a, b, c, d, e, f, g, h, k) {
                if (!c) c = 'div';
                var s = '', m = a && !a.indexOf('.') && a.indexOf('/') < 0;
                if (d) s += 'width:' + d + 'px;';
                if (h) s += h;
                return '<' + c + (s ? ' style="' + s + '"' : '') + ' class="i-c' + (g ? ' ' + g : '') + '">' + (m ? '<em class="i-c-g ' + a.replace(/\./g, '') : '<img src="' + (a || this.dot) + '" class="i-c-g') + '" onmouseover=$.class_add(this,"z-hv") onmouseout=$.class_del(this,"z-hv")' + (b ? ' onclick="' + js.s_quot(b) + '"' : '') + (e ? ' onload=$.imgfix(this,' + e + ',' + (f || e) + ')' : '') + ' align=absmiddle>' + (m ? '</em>' : '') + '<i class=va-i></i></' + c + '>';
            },
            icg: function (a) {
                return '<img src="' + (a && !a.indexOf('.') && a.indexOf('/') < 0 ? this.dot + '" class="i-c-g ' + a.replace(/\./g, '') : (a || this.dot) + '" class="i-c-g') + '" align=absmiddle>'
            },
            ico: function (src, x) {
                return x ? this.ic(src, x.clk, x.tag, x.w, x.icw, x.ich, x.clz, x.style) : this.ic(src)
            },
            sic: function (src, style, clz, clk) {
                return this.ic(src, clk, 'span', null, null, null, clz, style)
            },
            fic: function (src, style, clz, clk) {
                return this.ic(src, clk, null, null, null, null, 'c-fl' + (clz ? ' ' + clz : ''), style)
            }
        }, f: function (a, b, c, d) {
            return c ? this.f_vals(this.f(a), b, c, d) : (b ? this.f_el(a, b) : this.ww.document.forms[a || 0]);
        }, f_el: function (a, b) {
            if (typeof a == 'string') a = this.f(a);
            var o = a[b];
            return o ? (o.type == 'checkbox' || o.type == 'radio' ? [o] : o) : null;
        }, f_els: function (a, b, c) {
            if (typeof a === 'string') a = this.f(a);
            if (a.tagName === 'FORM') {
                if (!b) return a.elements;
                var o = a[b];
                return o ? (o.form ? [o] : o) : [];
            } else {
                var d = b ? $.ns(b, a) : $.query('input,select,textarea', a);
                if (c) {
                    d = Q(d).filter(function () {
                        for (var i = 0; i < c.length; i++) {
                            if (c[i].contains(this)) return false;
                        }
                        return true;
                    });
                }
                return d;
            }
        }, f_vals: function (a, b, c) {
            if (typeof a === 'string') a = this.f(a);
            var d = this.f_els(a, b, c);
            return d.length === 1 ? this.f_val(d[0], !b) : js.map(d, '$.f_val( v, ' + (!b) + ' )', true).join(b ? ',' : '&');
        }, f_val: function (a, b, c) {
            var d = a.name, v = a.value;
            if (!d || (b && a.disabled)) return '';
            switch (a.type) {
                case 'radio':
                    if (!a.checked) return '';
                    d = a.getAttribute('data-name') || d;
                    break;
                case 'checkbox':
                    if (!a.checked) return '';
                    break;
                case 'select-one':
                    if (a.selectedIndex < 0) return ''; else if (!v) return $._f_optval(a.options[a.selectedIndex], b);
                    break;
                case 'textarea':
                    if (!Br.css3) v = v.replace(/\\r/g, '');
            }
            return b ? (d + '=' + js.url_esc(v)) : v
        }, _f_optval: function (t, i, b) {
            var v, s = '';
            if (t.selected) {
                v = t.value;
                if (!v && !('value' in t)) v = t.text;
                s = b ? (o.name + '=' + js.url_esc(v)) : v;
            }
            return s;
        }, f_mul: function (a, b, c, d) {
            var e = this.ww.$(a), f, g = b === true;
            if (b === false) {
                if (e) {
                    this.wr(a);
                    this.rm(a);
                }
                return;
            }
            if (!e) {
                if (g) return false;
                $.append(d || this.db(), $.htm.hiframe(a));
                this.wr(a, '<html><head>' + $.htm.jstag() + '</head><body scroll=no><form name=' + a + ' method=post enctype=multipart/form-data target=' + a + 'up><div id=' + a + '-file></div><div id=' + a + '-in>' + (c || '') + '</div></form>' + $.htm.iframe(300, 240, '', a + 'up') + '</body></html>');
            }
            f = frames[a].document.forms[0];
            return b && !g ? f[b] : f;
        }, index: function (a) {
            if ('rowIndex' in a) return a.rowIndex;
            if ('cellIndex' in a) return a.cellIndex;
            var b = a.parentNode.children;
            for (var i = 0; i < b.length; i++) if (b[i] == a) return i;
            return -1;
        }, wr: function (a, b) {
            try {
                if (typeof a == 'string') a = this.ww.frames[a].document; else if (a.document) a = a.document;
                if (Br._ie6 && a.body) js.each(this.tags('textarea', a.body), ' v.style.display="none" ');
                a.open('text/html', 'replace');
                a.write(b == null ? '' : b);
                a.close();
            } catch (e) {
            }
        }, wopen: function (a, b) {
            var w = window.open(a, '', 'width=' + screen.availWidth + ',height=' + screen.availHeight + ',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
            if (w) w.focus();
        }, eid: function (a, b) {
            var c = Math.random(), d = String(c);
            return (a || '') + Math.floor(c * 9 + 1) + d.slice(2, b ? b + 1 : d.length);
        }, x_doc: function (a, b, c) {
            var d = Br.ie || Br.ie11 ? new ActiveXObject(Fn.Dom.msXmldomName()) : document.implementation.createDocument('', '', null);
            if ('setProperty' in d) d.setProperty('SelectionLanguage', 'XPath');
            if (a) {
                d.loadXML(a);
                var e = d.documentElement;
                if (!e && c) return d.parseError;
                if (b && !(Br.ie || Br.ie11)) b.importNode(e, true);
                return e;
            }
            return d;
        }, x_add: function (a, b, c, d) {
            if (!b) return;
            var e = a.nodeType === 9 ? a : a.ownerDocument, a = a.nodeType === 9 ? a.documentElement : a,
                x = typeof b === 'string' ? e.createElement(b) : (Br.ie || Br.ie11 ? b : e.importNode(b, true));
            if (typeof c === 'number') {
                var f = a.selectSingleNode('*[' + (c + 1) + ']');
                try {
                    a.insertBefore(x, f);
                } catch (ex) {
                    a.insertBefore($.x_doc(x.xml), f);
                }
            } else {
                try {
                    a.appendChild(x);
                } catch (ex) {
                    a.appendChild($.x_doc(x.xml));
                }
            }
            if (d != null) x.appendChild(e.createTextNode(d));
            var l = arguments.length;
            if (l > 4) {
                var e = arguments[4];
                if (l == 5 && typeof e == 'object') {
                    for (var i in e) x.setAttribute(i, e[i] == null ? '' : e[i]);
                } else {
                    for (var i = 4; i < l; i += 2) x.setAttribute(arguments[i], arguments[i + 1] == null ? '' : arguments[i + 1]);
                }
            }
            return x;
        }, x_obj: function (x, a, b) {
            var c = x && x.attributes;
            if (!c) return x;
            if (!a) a = {};
            for (var i = 0, l = c.length, d; i < l; i++) {
                d = c[i];
                if (!b || !a.hasOwnProperty(d.nodeName)) a[d.nodeName] = d.nodeValue;
            }
            return a;
        }, x_obi: function (x, a, b) {
            var c = x && x.attributes;
            if (!c) return x;
            if (!a) a = {};
            for (var i = 0, l = c.length, d, f; i < l; i++) {
                d = c[i];
                f = d.nodeValue;
                a[d.nodeName] = !b || js.ids_inc(b, d.nodeName) ? (!f || isNaN(f) ? f : parseFloat(f)) : f;
            }
            return a
        }, x_json: function (x) {
            var a = $.x_obj(x), b = $.xs(x, '*'), l = b.length;
            a.tagName = x.tagName;
            if (l) {
                a.nodes = [];
                for (var i = 0; i < l; i++) a.nodes.push($.x_json(b[i]));
            }
            return a
        }, x: function (x, q, c) {
            return c ? (this.x(x, q) || this.x_add(x, q)) : x.selectSingleNode(q);
        }, xs: function (x, q) {
            return x.selectNodes(q);
        }, xt: function (x, q, s) {
            var o = x.selectSingleNode(q), t = o && o.nodeType == 2;
            if (s == null) return o ? (t ? o.nodeValue : o.text) : '';
            if (t) o.nodeValue = s; else if (o) {
                if (Br.gck) o.appendChild(o.ownerDocument.createTextNode()).nodeValue = s; else o.text = s;
            }
        }, xc: function (a, b) {
            while (b && b != a) b = b.parentNode;
            return b ? true : false;
        }, xcdata: function (x, a) {
            if (a == null) return x;
            while (x.firstChild) x.removeChild(x.firstChild);
            try {
                x.appendChild(x.ownerDocument.createCDATASection(a));
            } catch (e) {
                x.appendChild(x.ownerDocument.createTextNode(a));
            }
            return x;
        }, xi: function (x, q, i) {
            return js.m($.xt(x, q, i));
        }, xp: function (x, q) {
            if (q) x = x.selectSingleNode(q);
            return x ? x.selectNodes('preceding-sibling::*').length : -1;
        }, x2p: function (a, b) {
            var c = $.xp(a) - $.xp(b);
            if (c) a.parentNode.insertBefore(a, c > 0 ? b : a.nextSibling);
        }, xrm: function (x) {
            if (x) x.parentNode.removeChild(x);
        }, xra: function (x) {
            for (var i = 1; i < arguments.length; i++) x.removeAttribute(arguments[i]);
            return x;
        }, xpath: function (a, b, c, d) {
            if (d) {
                a = 'translate(@' + a + ',"ABCDEFGHIJKLMNOPQRSTUVWXYZ","abcdefghijklmnopqrstuvwxyz")';
                b = b.toLowerCase();
            } else a = '@' + a;
            if (!c) c = '=';
            return c.indexOf('=') > -1 ? a + c + '"' + b + '"' : c + '(' + a + ',"' + b + '")';
        }, r_s: function (a) {
            if (!a) a = this.ww;
            return Br.ie ? a.document.selection : a.getSelection();
        }, r_r: function (a) {
            if (!a) a = this.ww;
            return Br.ie ? a.document.selection.createRange() : a.getSelection().getRangeAt(0);
        }, r_o: function (a, b) {
            if (!a) a = this.ww;
            var c = this.r_s(a);
            if (Br.ie) {
                return c.type == 'Control' ? c.createRange().item(0) : (b ? c.createRange().parentElement() : null);
            } else {
                c = c.getRangeAt(0).cloneContents().firstChild;
                if (c) return c.nodeType == 1 ? c : (b ? c.parentElement : null);
            }
        }, r_focus: function (a) {
            if (a.createTextRange) {
                var r = a.createTextRange();
                r.moveStart("character", a.value.length);
                r.collapse(true);
                r.select();
            } else {
                a.setSelectionRange(a.value.length, a.value.length);
                a.focus();
            }
        }, g_z: function (a) {
            if (a != null) {
                a = js.m(a);
                if (a > Fn.Dom._z) Fn.Dom._z = a + 1;
                return a;
            }
            return Fn.Dom._z++;
        }, clbd: function (a) {
            clipboardData.setData('text', typeof a == 'string' ? a : a.outerHTML);
        }, replaceWith: function (a, b) {
            if (typeof b == 'string') {
                var n = a.nextSibling, p = a.parentNode;
                $.rm(a);
                return n ? $.before(n, b) : $.append(p, b);
            } else {
                if (Br.w3c) b = Fn.Dom.importNode(a, b);
                a.parentNode.replaceChild(b, a);
                return b;
            }
        }, opacity: function (el, _opacity) {
            if (_opacity == null) {
                var a, r;
                if (Br.css3) {
                    a = el.currentStyle.opacity;
                    r = a === '' ? 100 : js.m(a) * 100;
                } else {
                    a = el.currentStyle.filter.match(/opacity=(\d+)/i);
                    r = a ? js.m(a[1]) : 100;
                }
                return r;
            } else {
                _opacity = js.m_rng(_opacity, 0, 100);
                if (Br.css3) {
                    el.style.opacity = _opacity === 100 ? '' : _opacity / 100;
                } else {
                    if (_opacity === 100) {
                        el.style.cssText = el.style.cssText.replace(/filter:[^;]+;/i, '');
                    } else el.style.filter = 'alpha(opacity=' + _opacity + ')';
                }
            }
        }, fadeIn: function (el, fn) {
            var _opacity = $.opacity(el);
            return js.fps_timer(function (p) {
                $.opacity(el, p === 0 ? _opacity : Math.ceil((100 - _opacity) * p));
                if (p === 1) {
                    $.class_del(el, 'opc0');
                    if (fn) fn();
                }
            }, 150, 15);
        }, fadeOut: function (el, rm, fn) {
            var _opacity = $.opacity(el);
            return js.fps_timer(function (p) {
                $.opacity(el, Math.ceil(_opacity * (1 - p)));
                if (p == 1) {
                    if (fn) fn();
                    if (rm !== false) $.rm(el);
                }
            }, 150, 15);
        }, slideDown: function (a, h, fn) {
            js.fps_timer(function (p) {
                if (fn && p === 1) {
                    a.style.height = 'auto';
                    fn && fn();
                } else {
                    a.style.height = (h * p) + 'px';
                }
            });
        }, slideOut: function (el, fn, hrz, rm) {
            var _opacity = $.opacity(el), _dist = hrz ? $.boxwd(el) : $.boxht(el);
            $.class_add(el, 'no-overflow');
            return js.fps_timer(function (p) {
                if (p == 1) {
                    if (fn) fn();
                    if (rm !== false) $.rm(el);
                } else {
                    $.opacity(el, Math.ceil(_opacity * (1 - p)));
                    el.style[hrz ? 'width' : 'height'] = Math.ceil(_dist - (_dist * p)) + 'px';
                }
            });
        }, animate: function (el, x) {
            if (x.type === 'background') {
                function color(k, p) {
                    return p < 1 / 3 ? k + Math.floor((255 - k) * p * 3) : p < 2 / 3 ? 255 - Math.floor((255 - k) * (p - 1 / 3) * 3) : k + Math.floor((255 - k) * (p - 2 / 3) * 3);
                };var v = x.value || '#fceaa3', a = parseInt(v.substr(1, 2), 16), b = parseInt(v.substr(3, 2), 16),
                    c = parseInt(v.substr(5, 2), 16);
                js.fps_timer(function (p) {
                    el.style.backgroundColor = 'rgb(' + color(a, p) + ',' + color(b, p) + ',' + color(c, p) + ')';
                    if (p === 1) el.style.backgroundColor = '';
                }, 400, 40);
            } else if (x.type === 'top') {
                var f = x.from || -40, t = x.to || 0, s = x.speed || 200;
                el.style.position = 'relative';
                el.style.marginTop = f + 'px';
                js.fps_timer(function (p) {
                    el.style.marginTop = ((1 - p) * (f - t)) + 'px';
                    if (p === 1) el.style.marginTop = '';
                }, s, Math.ceil(s / 5));
            }
        }
    }
});
Fn._register_('FormCheck', {
    Const: function (a) {
        this._a = {};
        this.rng = a;
    }, Helper: {
        _check_size: function (b, c, l) {
            if (c.mns && l < js.m(c.mns)) m = c.mns_msg ? js.s_printx(c.mns_msg, b) : Loc.ps(Loc.choose_min, b.t, c.mns); else if (c.mxs && l > js.m(c.mxs)) m = c.mxs_msg ? js.s_printx(c.mxs_msg, b) : Loc.ps(Loc.choose_max, b.t, c.mxs);
            return m;
        }, _check_time: function (b, c, nt, xt, v, rl, v1) {
            var m;
            if (v && js.d_valid(v, b.df)) {
                var g = js.d_ps(v, b.df);
                if (c.mnt && nt > g) m = c.mnt_msg ? s.s_printx(c.mnt_msg, b) : Loc.ps(Loc.form.time_mnt, b.t, c.mnt); else if (c.mxt && xt < g) m = c.mxt_msg ? js.s_printx(c.mxt_msg, b) : Loc.ps(Loc.form.time_mxt, b.t, c.mxt);
                if (!m && rl && v1 && js.d_valid(v1, b.df) && !eval('js.d_ps(v1,b.df)' + rl + 'g')) m = c.rl_msg ? js.s_printx(c.rl_msg, b) : Loc.ps(Loc.form.time_start_end, b.t, Loc[rl]);
            } else m = Loc.ps(Loc.form.time_error, b.t);
            return m;
        }, _check_ntn: function (b, c, v, a) {
            if (c.ntn == 1 && !v) return c.ntn_msg ? js.s_printx(c.ntn_msg, b) : (b.t ? (a ? b.t + ': ' + Loc.choose_atleastone : Loc.ps(Loc.form.notnull, b.t)) : Loc.please_complete_required);
        }, _valid: function (b, c, d) {
            var v = js.s_trim(d), m;
            if (b.graytip && v == b.graytip) v = '';
            if (m = this._check_ntn(b, c, v)) return m;
            if (c.mxl) {
                var g = js.s_wd(v, false, Cfg.cn_bytes);
                if (g > c.mxl && (m = c.mxl_msg ? js.s_printx(c.mxl_msg, b) : Loc.ps(Loc.form.toolong, b.t, c.mxl, Math.floor(c.mxl / Cfg.cn_bytes), g - js.m(c.mxl)))) return m;
            }
            if (c.mnl) {
                var g = js.s_wd(v, false, Cfg.cn_bytes);
                if (g < c.mnl && (m = c.mnl_msg ? js.s_printx(c.mnl_msg, b) : Loc.ps(Loc.form.tooshort, b.t, c.mnl, js.m(c.mnl) - g))) return m;
            }
            if (c.mxv || c.mnv) {
                if ((isNaN(v) || v != d) && (m = Loc.ps(Loc.form.number_valid, b.t))) return m;
                if (c.mnv && !isNaN(c.mnv) && v < js.m(c.mnv) && (m = c.mnv_msg ? js.s_printx(c.mnv_msg, b) : Loc.ps(Loc.form.number_gt, b.t, js.m(c.mnv)))) return m;
                if (c.mxv && !isNaN(c.mxv) && v > js.m(c.mxv) && (m = c.mxv_msg ? js.s_printx(c.mxv_msg, b) : Loc.ps(Loc.form.number_lt, b.t, js.m(c.mxv)))) return m;
            }
            if (c.mxs || c.mns && (m = this._check_size(b, c, v.split(',').length))) return m;
            if (c.match && v && !eval(c.match + '.test(v)') && (m = c.match_msg ? js.s_printx(c.match_msg, b) : Loc.ps(Loc.form.nomatch, b.t))) return m;
            if (v && (b.tp === 'date' || b.tp === 'dateperiod')) {
                var nt = c.mnt && js.d_ps(c.mnt, b.df), xt = c.mxt && js.d_ps(c.mxt, b.df);
                if (m = this._check_time(b, c, nt, xt, v)) return m;
            }
        }, _valid_arr: function (b, c, v) {
            if (c) {
                for (var i = 0, m; i < c.length; i++) if (m = this._valid(b, c[i], v)) return m;
            }
        }
    }, Prototype: {
        add: function (a) {
            if (a) this._a[a.n] = a;
            return this;
        }, rm: function (a) {
            if (this._a[a]) delete this._a[a];
        }, get: function (a) {
            return this._a[a];
        }, uncheck: function (a, b) {
            var c = this._a[a];
            if (c) c.uncheck = b == null ? 1 : b;
        }, g_error: function (a) {
            var r = [], f = this.rng;
            for (var i in this._a) {
                var b = this._a[i], c = a.get(i), m = '';
                if (b.uncheck) continue;
                if (b.hdl) {
                    m = b.hdl(b, c && c[0]);
                } else if (c) {
                    var n = b.tp === 'radio' ? b.vmid + '-' + i : i, e = $.f_els(f, n), g;
                    for (var j = 0, d; j < c.length; j++) {
                        d = c[j];
                        if (e.length > 1) {
                            if (js.any(e, '!v.disabled')) {
                                if (d.ntn && !$.f_vals(f, n)) m = Fn.FormCheck._check_ntn(b, d, '', true);
                                if (!m && (c.mns || c.mxs)) m = Fn.FormCheck._check_size(b, d, js.select(e, 'v.checked').length);
                            }
                        } else {
                            g = e[0];
                            if (g && !g.disabled) switch (b.tp) {
                                case 'select':
                                    if (d.ntn && !$.f_vals(f, i)) m = Fn.FormCheck._check_ntn(b, d, '', true);
                                    break;
                                case 'radio':
                                case 'checkbox':
                                    if (d.ntn && !g.checked) m = Fn.FormCheck._check_ntn(b, d, '', true);
                                    break;
                                default:
                                    var v = g.value;
                                    m = Fn.FormCheck._valid(b, d, v);
                                    if (b.n1) {
                                        var nt = d.mnt && js.d_ps(d.mnt, b.df), xt = d.mxt && js.d_ps(d.mxt, b.df),
                                            m1 = '', o1 = $.f_els(f, b.n1)[0], v1 = js.s_trim(o1.value);
                                        if (v1 && !js.d_valid(v1, b.df)) m1 = Loc.ps(Loc.form.time_error, b.t1 || b.t);
                                        if (d.ntn && !v1) m1 = Fn.FormCheck._check_ntn(b, d, v1);
                                        if (!o1.disabled && v && v1) m1 = Fn.FormCheck._check_time(b, d, nt, xt, v1, b.rl || '<=', v);
                                        if (m1) r.push({msg: m1, n: b.n1, tp: b.tp});
                                    }
                                    break;
                            }
                        }
                        if (m) break;
                    }
                } else {
                    if (b.tp === 'date' || b.tp === 'dateperiod') {
                        var o = $.f_els(f, b.n)[0], v = o.value;
                        if (v && !js.d_valid(v, b.df)) m = Loc.ps(Loc.form.time_error, b.t);
                        if (b.n1) {
                            var o1 = $.f_els(f, b.n1)[0], v1 = js.s_trim(o1.value), m1;
                            if (v1 && !js.d_valid(v1, b.df)) m1 = Loc.ps(Loc.form.time_error, b.t1 || b.t);
                            if (!o.disabled && !o1.disabled && v && v1) {
                                m1 = Fn.FormCheck._check_time(b, {}, nt, xt, v1, b.rl || '<=', v);
                            }
                            if (m1) r.push({msg: m1, n: b.n1, tp: b.tp});
                        }
                    }
                }
                if (m) r.push({msg: m, n: b.n, tp: b.tp});
            }
            return r;
        }, dispose: function () {
            this._a = this.rng = null;
        }
    }
});
Fn._register_('JS', {
    Const: function (a, m) {
        if (a) a.eval('(' + Fn.JS.init + ')( "' + (m || '') + '" )')
    },
    Helper: {
        uidCnt: 0, _s: new String('v'), _sa: new String('v,i'), F: function (a) {
            return this.AF(a, true, this._s);
        }, AF: function (a, b, c) {
            if (!a) return js.K;
            if (!b) typeof a == 'string' ? (a = 'return ' + a) : (a[0] = 'return ' + a[0]);
            return this._F[typeof a == 'string' ? 'call' : 'apply'](c == null ? this._sa : c, a);
        }, _F: function (a) {
            var l = arguments.length;
            if (l > 1) {
                for (var i = 0, r = this; i < l - 1; i++) r += ',$' + i;
                if (!this.length) r = r.substr(1);
                return js.bind.apply(null, [Function(r, a), null].concat(js.argu(1)));
            }
            return Function(this, a);
        }, qi: ['each'], _q: function (a, b, c) {
            return js[this.qi[b]].apply(js, [a].concat(c));
        }, JSON: (function () {
            var JSON = {};

            function f(n) {
                return n < 10 ? '0' + n : n;
            };var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
                es = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
                gap, _indent,
                meta = {'\b': '\\b', '\t': '\\t', '\n': '\\n', '\f': '\\f', '\r': '\\r', '"': '\\"', '\\': '\\\\'}, rep;

            function quote(s) {
                es.lastIndex = 0;
                return es.test(s) ? '"' + s.replace(es, function (a) {
                    var c = meta[a];
                    return typeof c === 'string' ? c : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                }) + '"' : '"' + s + '"';
            };

            function str(key, h) {
                var i, k, v, length, mind = gap, _partial, u = h[key];
                if (u && typeof u === 'object' && u instanceof Date) {
                    u = u.getUTCFullYear() + '-' + f(u.getUTCMonth() + 1) + '-' + f(u.getUTCDate()) + 'T' + f(u.getUTCHours()) + ':' + f(u.getUTCMinutes()) + ':' + f(u.getUTCSeconds()) + 'Z';
                }
                if (typeof rep === 'function') {
                    u = rep.call(h, key, u);
                }
                switch (typeof u) {
                    case 'string':
                        return quote(u);
                    case 'number':
                        return isFinite(u) ? String(u) : 'null';
                    case 'boolean':
                    case 'null':
                        return String(u);
                    case 'object':
                        if (!u) {
                            return 'null';
                        }
                        gap += _indent;
                        _partial = [];
                        if (js.isArray(u)) {
                            length = u.length;
                            for (i = 0; i < length; i += 1) {
                                _partial[i] = str(i, u) || 'null';
                            }
                            v = _partial.length === 0 ? '[]' : gap ? '[\n' + gap + _partial.join(',\n' + gap) + '\n' + mind + ']' : '[' + _partial.join(',') + ']';
                            gap = mind;
                            return v;
                        }
                        if (rep && typeof rep === 'object') {
                            length = rep.length;
                            for (i = 0; i < length; i += 1) {
                                k = rep[i];
                                if (typeof k === 'string') {
                                    v = str(k, u);
                                    if (v) {
                                        _partial.push(quote(k) + (gap ? ': ' : ':') + v);
                                    }
                                }
                            }
                        } else {
                            for (k in u) {
                                if (Object.hasOwnProperty.call(u, k)) {
                                    v = str(k, u);
                                    if (v) {
                                        _partial.push(quote(k) + (gap ? ': ' : ':') + v);
                                    }
                                }
                            }
                        }
                        v = _partial.length === 0 ? '{}' : gap ? '{\n' + gap + _partial.join(',\n' + gap) + '\n' + mind + '}' : '{' + _partial.join(',') + '}';
                        gap = mind;
                        return v;
                }
            };
            if (typeof JSON.stringify !== 'function') {
                JSON.stringify = function (u, r, s) {
                    var i;
                    gap = '';
                    _indent = '';
                    if (typeof s === 'number') {
                        for (i = 0; i < s; i += 1) {
                            _indent += ' ';
                        }
                    } else if (typeof s === 'string') {
                        _indent = s;
                    }
                    rep = r;
                    if (r && typeof r !== 'function' && (typeof r !== 'object' || typeof r.length !== 'number')) {
                        throw new Error('JSON.stringify');
                    }
                    return str('', {'': u});
                };
            }
            if (typeof JSON.parse !== 'function') {
                JSON.parse = function (t, r) {
                    var j;

                    function walk(h, key) {
                        var k, v, u = h[key];
                        if (u && typeof u === 'object') {
                            for (k in u) {
                                if (Object.hasOwnProperty.call(u, k)) {
                                    v = walk(u, k);
                                    if (v !== undefined) {
                                        u[k] = v;
                                    } else {
                                        delete u[k];
                                    }
                                }
                            }
                        }
                        return r.call(h, key, u);
                    };t = String(t);
                    cx.lastIndex = 0;
                    if (cx.test(t)) {
                        t = t.replace(cx, function (a) {
                            return '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                        });
                    }
                    if (/^[\],:{}\s]*$/.test(t.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
                        j = eval('(' + t + ')');
                        return typeof r === 'function' ? walk({'': j}, '') : j;
                    }
                    throw new SyntaxError('JSON.parse');
                };
            }
            return JSON;
        }())
    },
    Prototype: {
        clone: Object.clone,
        clonex: Object.clonex,
        clonei: Object.clonei,
        extend: Object.extend,
        implement: Object.extendz,
        n: 0,
        A: [],
        K: function (a) {
            return a
        },
        I: function (i) {
            return this[i]
        },
        rt_false: function () {
            return false
        },
        array: function () {
            var a = js.A.concat.apply(js.A, arguments);
            a.item = this.I;
            return a;
        },
        uid: function (a) {
            var b = (Fn.JS.uidCnt++).toString(32);
            return a ? a + b : b;
        },
        fid: function (a) {
            return a['fish:id'] || (a['fish:id'] = js.uid())
        },
        argu: function () {
            return js.A.slice.apply(arguments.callee.caller.arguments, arguments);
        },
        bind: function (a, b) {
            var c = js.argu(2);
            if (typeof a == 'string') a = Function(a);
            return function () {
                return a.apply(b, js.A.slice.call(arguments).concat(c));
            }
        },
        fp: function (a, b) {
            var c = js.argu(2);
            return function () {
                return a[b].apply(a, js.argu().concat(c));
            }
        },
        call: function (a, b) {
            return (typeof a === 'function' ? a : Function(a)).call(b)
        },
        apply: function (a, b, c) {
            return (typeof a === 'function' ? a : Function(a)).apply(b, c || js.A)
        },
        map: function (x, a, b, c) {
            var r = [], i = 0, l = x.length, m = c ? 'apply' : 'call';
            if (!l || !a) {
                if (!a) for (; i < l; i++) r.push(x[i]);
                return r;
            }
            if (typeof a != 'function') a = Fn.JS.AF(a);
            if (b) for (var v; i < l; i++) {
                if (v = a[m](x[i], x[i], i)) r.push(v)
            } else for (; i < l; i++) {
                r.push(a[m](x[i], x[i], i))
            }
            return r;
        },
        each: function (x, a, b, c) {
            var i = 0, l = x.length, m = c ? 'apply' : 'call';
            if (!l) return x;
            if (typeof a != 'function') a = Fn.JS.AF(a, true);
            if (b) for (i = l - 1; i >= 0; i--) {
                a[m](x[i], x[i], i)
            } else for (; i < l; i++) {
                a[m](x[i], x[i], i)
            }
            return x;
        },
        any: function (x, a, b) {
            var i = 0, l = x.length, m = b ? 'apply' : 'call', r = false;
            if (!l) return r;
            if (typeof a != 'function') a = Fn.JS.AF(a);
            for (; i < l && !r; i++) {
                r = a[m](x[i], x[i], i)
            }
            return r;
        },
        all: function (x, a, b) {
            var i = 0, l = x.length, m = b ? 'apply' : 'call', r = true;
            if (!l) return r;
            if (typeof a != 'function') a = Fn.JS.AF(a);
            for (; i < l; i++) {
                if (!a[m](x[i], x[i], i)) return false
            }
            return r;
        },
        find: function (x, a, b, c) {
            if (!x) return c ? -1 : null;
            if (typeof a != 'function') a = Fn.JS.AF(a);
            var i = 0, l = x.length;
            for (; i < l; i++) {
                if (b && x[i].length) {
                    var d = js.find(x[i], a, b);
                    if (d !== null) return d
                }
                if (a.call(x[i], x[i], i)) return c ? i : x[i];
            }
            return c ? -1 : null;
        },
        select: function (x, a, b) {
            if (!a) return this.map(x);
            if (typeof a != 'function') a = Fn.JS.AF(a);
            var i = 0, l = x.length, m = b ? 'apply' : 'call', r = [];
            for (; i < l; i++) if (a[m](x[i], x[i], i)) r.push(x[i]);
            return r;
        },
        search: function (a, b, c, d) {
            if (typeof a != 'function') a = Fn.JS.AF(a);
            if (c == null) c = 0;
            if (d == null) d = this.length - 1;
            var i, v;
            while (c <= d) {
                i = (c + d) >> 1;
                v = a(this[i], i, this);
                if (v < b) c = i + 1; else if (v > b) d = i - 1; else return i;
            }
            return -(c + 1)
        },
        pop: function (x, a, b) {
            if (!x) return null;
            var i = 0, l = x.length, d = false;
            if (arguments.length === 3) d = true; else if (typeof a === 'string') a = Fn.JS.AF(a);
            for (; i < l; i++) {
                if (d ? x[i][a] == b : a.call(x[i], x[i], i)) {
                    return x.splice(i, 1)[0];
                }
            }
            return null;
        },
        sum: function (a, b) {
            for (var i = 0, l = a.length, r = 0, v; i < l; i++) if (b) {
                if (!isNaN(a[i])) r = js.m_add(r, parseFloat(a[i]));
            } else r = js.m_add(js.m(r), js.m(a[i]));
            return r;
        },
        index: function (a, b, c) {
            for (var i = 0, l = a.length; i < l; i++) if (c ? (a[i][c] == b[c]) : (a[i] == b)) return i;
            return -1
        },
        inArray: function (a, b) {
            return js.index(a, b) > -1
        },
        isArray: function (a) {
            return Object.prototype.toString.apply(a) === '[object Array]' && typeof a === 'object'
        },
        unique: function (a) {
            var b = [];
            for (var i = 0; i < a.length; i++) if (js.index(b, a[i]) < 0) b.push(a[i]);
            return b;
        },
        push: function (a, b, c) {
            if (c && a.length) {
                for (var i = 0; i < b.length; i++) if (js.index(a, b[i]) < 0) a.push(b[i]);
            } else a.push.apply(a, b);
            return a;
        },
        remove: function (a, b) {
            if (a) {
                for (var i = 0, l = a.length; i < l; i++) {
                    if (a[i] == b) {
                        a.splice(i, 1);
                        break;
                    }
                }
            }
            return a;
        },
        join: function (a) {
            if (typeof a == 'object') for (var i = 1; i < arguments.length; i++) {
                a.push.apply(a, arguments[i]);
            }
            return a;
        },
        type: function (a) {
            if (a == null) return 'undefined';
            var b = typeof a;
            if (b == 'object') {
                if (a.ownerDocument) return a.ownerDocument.body ? 'html' : 'xml';
                if (this.isArray(a)) return 'array';
            }
            return b;
        },
        s_p0: function (a, b, c) {
            if (c == null) c = '0';
            if (!b) return a < 10 ? c + a : a;
            a = String(a);
            while (a.length < b) a = c + a;
            return a
        },
        s_rpt: function (a, b, c) {
            for (var i = 0, d = []; i < b; i++) d.push(a);
            return d.join(c == null ? ',' : c);
        },
        s_trim: function (a) {
            return a ? a.replace(/^\s+|\s+$/g, '') : '';
        },
        s_fr: function (a, b, c) {
            var d = c ? a.lastIndexOf(b) : a.indexOf(b);
            return d < 0 ? '' : a.substr(d + b.length)
        },
        s_to: function (a, b, c) {
            var d = c ? a.lastIndexOf(b) : a.indexOf(b);
            return d < 0 ? '' : a.substr(0, d)
        },
        s_up: function (a) {
            return a.charAt(0).toUpperCase() + a.substr(1);
        },
        s_wd: function (s, b, c) {
            if (!s) return 0;
            if (!c) c = 2;
            if (b) {
                s = s.replace(/[^\x00-\xff]/g, js.s_p0('-', c)).replace(/&#\d{2,3};/g, '-').replace(/&#\d{4,5};/g, '--').replace(/<\/?\w+(?: [^>]+)?>/g, '').replace(/&[a-z]+?;/g, '-');
                return s.length;
            } else {
                for (var i = 0, d = 0, l = s.length; i < l; i++) {
                    d += s.charCodeAt(i) > 128 ? c : 1;
                }
                return d;
            }
        },
        s_px: function (s, b, i) {
            if (!s) return 0;
            return this.s_wd(s, 1) * (i ? Math.ceil(i / 2) : 6) + (b ? s.length : 0);
        },
        s_printf: function (s) {
            if (s) {
                var a = js.argu(1);
                return s.replace(/\$(\d+)/g, function (b, c) {
                    return a[c]
                });
            }
        },
        s_printa: function (s, a, b) {
            if (!s) return '';
            var c = s.match(/^\$\{?(\d+)\}?$/);
            if (c) {
                return typeof a[c[1]] == 'object' ? a[c[1]] : (b ? js.url_esc(a[c[1]]) : a[c[1]]);
            }
            return a ? s.replace(/\$\{?(\d+)\}?/g, function (c, d) {
                c = a[d] || '';
                return b ? js.url_esc(c) : c
            }) : s;
        },
        _s_comma_meta: {
            '\t': '\\x09',
            '\n': '\\x0a',
            '\r': '\\x0d',
            ' ': '\\x20',
            '"': '\\x22',
            "'": '\\x27',
            '\\': '\\x5c'
        },
        s_comma: function (s) {
            return s.replace(/[\\\s\'\"]/g, function (c) {
                return js._s_comma_meta[c]
            })
        },
        s_printx: function (s, x, b, y) {
            if (!s || !x) return s || '';
            var c = 'getAttribute' in x;
            if (s.indexOf('$COMMA') > -1) s = s.replace(/\$COMMA\{(\w+)\}/g, function (d, e) {
                d = (c ? x.getAttribute(e) : x[e]) || '';
                e = d.replace(/[\\\s\'\"]/g, function (s) {
                    return js._s_comma_meta[s]
                });
                return y ? js.url_esc(e) : e;
            });
            s = s.replace(/\$\{?([a-z_]\w*)\}?/gi, function (d, e) {
                d = (c ? x.getAttribute(e) : x[e]) || '';
                e = b ? d.replace(/[\\\s\'\"]/g, function (s) {
                    return js._s_comma_meta[s]
                }) : d;
                return y ? js.url_esc(e) : e;
            });
            return s;
        },
        s_h2t: function (s) {
            if (s == null) return s;
            s = String(s);
            return (s || '').replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/'/g, '&#39;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
        },
        s_t2h: function (s) {
            if (s == null) return s;
            s = String(s);
            return (s || '').replace(/&quot;/g, '"').replace(/&#39;/g, "'").replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&');
        },
        s_quot: function (s) {
            if (s == null) return s;
            s = String(s);
            return s ? (s.indexOf('"') < 0 ? s : s.replace(/"/g, '&quot;')) : '';
        },
        s_attr: function (a) {
            return js.s_h2t(typeof a === 'string' ? a : js.toJSONString(a))
        },
        s_byte: function (a, b, c, n) {
            var d = 0, e, f = c ? js.s_wd(c) : 0, k, i = 0, l = a.length;
            for (; i < l; i++) {
                e = a.charCodeAt(i) > 128 ? 2 : 1;
                if (n && a.charAt(i) === '&') {
                    if (k = a.slice(i).match(/^&(?:#(\d{2,5})|[a-z]{2,});/i)) {
                        if ((k[1] && k[1] > 256)) e = 2;
                        i += k[0].length - 1;
                    }
                }
                if (d + e > b - f) {
                    break;
                }
                d += e;
                if (d === b - f) {
                    i++;
                    break;
                }
            }
            if (i === a.length) return a;
            if (c) {
                e = a.slice(i);
                k = arguments.callee(e, f, false, n);
                return a.slice(0, i) + (e === k ? e : c)
            }
            return a.substr(0, i)
        },
        ids_add: function (s, n, p) {
            if (!p) p = ',';
            return js.ids_inc(s, n, p) ? s : (s ? s + p + n : n);
        },
        ids_inc: function (s, n, p) {
            if (!s || !n) return false;
            if (s == n) return true;
            if (!p) p = ',';
            return (p + s + p).indexOf(p + n + p) > -1;
        },
        ids_del: function (s, n, p) {
            if (s == null) return '';
            if (!p) p = ',';
            return (p + s + p).replace(p + n + p, p).slice(1, -1)
        },
        url_esc: function (a) {
            return a == null ? '' : encodeURIComponent(a)
        },
        url_unesc: function (a) {
            return decodeURIComponent(a.replace(/\+/g, ' '))
        },
        url_get: function (a, b) {
            a = a || document.location.href;
            if (b) return (a.match(new RegExp('[\\?&]' + b + '=([^&]*)'), 'g') || false)[1]; else {
                var r = {}, c = a.split('?');
                c = c[c.length - 1];
                if (c) {
                    c = c.split('&');
                    for (var d = 0, e, f, g; d < c.length; d++) {
                        e = c[d].indexOf('=');
                        if (e == -1) continue;
                        f = c[d].substring(0, e);
                        g = c[d].substring(e + 1);
                        r[f] = this.url_unesc(g.replace(/\+/g, ' '));
                    }
                }
                return r;
            }
        },
        url_set: function (a, b, c) {
            if (a == null) a = '';
            if (b === '#') {
                return /#.*/.test(a) ? a.replace(/#.*/, '#' + c) : a + '#' + c;
            } else if (arguments.length > 3) {
                for (var i = 1, s = a; i < arguments.length; i += 2) {
                    s = arguments.callee(s, arguments[i], arguments[i + 1]);
                }
                return s;
            } else if (typeof b === 'object') {
                var s = a;
                for (var i in b) s = arguments.callee(s, i, b[i]);
                return s;
            } else if (b.indexOf('=') > 0) {
                return a + (a.indexOf('?') < 0 ? '?' : '&') + b;
            } else if (b) {
                var v = a.indexOf('vm:|') === 0 ? js.s_fr(a, '|').split('|') : null;
                if (v) a = 'vm:|' + v[0];
                c = js.url_esc(typeof c === 'object' ? js.toJSONString(c) : c);
                var d = new RegExp('\\b' + b + '=[^&]*'),
                    e = d.test(a) ? a.replace(d, b + '=' + c) : a + (a.indexOf('?') < 0 ? '?' : '&') + b + '=' + c;
                return v ? (v[0] = e, v).join('|') : e;
            }
        },
        url_del: function (a, b) {
            return a.replace(new RegExp('[\\?&]' + b + '=[^&]*'), '');
        },
        url_loc: function (a) {
            var b = a.match(/^(\w+:)\/\/([\w.]+)?(:\d+)?([^?#]+)?(\?[^#]+)?(#.+)?/);
            return b && {
                home: b[1] + '//' + b[2] + b[3],
                host: b[2] + b[3],
                hostname: b[2],
                port: b[3].substr(1),
                pathname: b[4],
                search: b[5],
                hash: b[6]
            };
        },
        url_parse: function (a, b) {
            a = js.s_to(a, '/', true);
            return b.indexOf('./') === 0 ? js.url_parse(a + '/', b.slice(2)) : b.indexOf('../') === 0 ? js.url_parse(a, b.slice(3)) : b.charAt(0) === '/' ? b : a + '/' + b
        },
        d_I: 60000,
        d_H: 3600000,
        d_D: 86400000,
        d_sf: 'yyyy-mm-dd hh:ii:ss',
        d_sd: new Date('2008/01/01'),
        d_df: function (a) {
            switch (String(a)) {
                case '-1':
                case 'datetimes':
                    return 'yyyy-mm-dd hh:ii:ss';
                case '1':
                case 'date':
                    return 'yyyy-mm-dd';
                case 'datetime':
                default:
                    return 'yyyy-mm-dd hh:ii';
            }
        },
        d_ps: function (s, f) {
            if (f) {
                s = this.d_f(this.d_sd, this.d_sf.replace(f.replace(/\b(\w)\b/g, '$1$1'), s));
            } else {
                var b = s.split('-');
                if (b.length === 1) s += '-01-01'; else if (b.length === 2) s += '-01';
            }
            var a = new Date(s.replace(/-/g, '/'));
            if (isNaN(a)) a = new Date(s.split(' ')[0].replace(/-/g, '/'));
            return isNaN(a) ? new Date : a;
        },
        d_f: function (a, b) {
            var o = {
                y: a.getFullYear(),
                m: a.getMonth(),
                d: a.getDate(),
                h: a.getHours(),
                i: a.getMinutes(),
                s: a.getSeconds(),
                w: (a.getDay() || 7)
            };
            return b.toLowerCase().replace('yyyy', o.y).replace('yy', o.y % 100).replace('mm', this.s_p0(o.m + 1)).replace('dd', this.s_p0(o.d)).replace('hh', this.s_p0(o.h)).replace('ii', this.s_p0(o.i)).replace('ss', this.s_p0(o.s)).replace('m', o.m + 1).replace('d', o.d).replace('h', o.h).replace('i', o.i).replace('s', o.s);
        },
        d_valid: function (a, b) {
            a = a.replace(/\b(\d)\b/g, '0$1');
            return a == this.d_f(this.d_ps(a, b), b);
        },
        d_add: function (a, b, c) {
            var e = a;
            if (c) {
                var d = 'y/m/d/h/i/s'.replace(b, c).replace(/[a-z]/g, '0').split('/'),
                    e = new Date(a.getFullYear() + parseInt(d[0]), a.getMonth() + parseInt(d[1]), a.getDate() + parseInt(d[2]), a.getHours() + parseInt(d[3]), a.getMinutes() + parseInt(d[4]), a.getSeconds() + parseInt(d[5]));
                if ((b == 'y' || b == 'm') && e.getDate() != a.getDate()) e = new Date(e.getTime() - e.getDate() * this.d_D);
            }
            if (arguments.length > 3) {
                for (var i = 3; i < arguments.length; i += 2) if (arguments[i + 1]) e = arguments.callee.call(this, e, arguments[i], arguments[i + 1]);
            }
            return e;
        },
        d_same: function (a, b) {
            if (!b) b = new Date;
            return a.getDate() === b.getDate() && a.getMonth() === b.getMonth() && a.getFullYear() === b.getFullYear()
        },
        d_day: function (a, b) {
            return new Date(a.getFullYear(), a.getMonth(), a.getDate(), 0, 0, 0, b || 0);
        },
        d_wki: function (a) {
            var d = a.getDay() - 1;
            return d < 0 ? 6 : d
        },
        d_wk: function (a, b, c) {
            if (b == null) b = 1;
            if (c == null) c = 1;
            var w = a.getDay();
            if (!w) w = 7;
            var d = new Date(a.getFullYear(), a.getMonth(), a.getDate() - w + b),
                e = Math.ceil((d - new Date(d.getFullYear(), 0, 0)) / js.d_D),
                f = new Date(a.getFullYear(), a.getMonth(), a.getDate() - w + c);
            return [d.getFullYear(), Math.floor((e + 6) / 7), f, new Date(f.getTime() + (7 * js.d_D) - 1)];
        },
        d_t: function () {
            return new Date().getTime()
        },
        r_i_z: /^[1-9]\d*$/,
        r_i_f: /^-[1-9]\d*$/,
        r_i: /^-?[1-9]\d*$/,
        r_i_ff: /^[1-9]\d*|0$/,
        r_i_fz: /^-[1-9]\d*|0$/,
        r_f_z: /^[1-9]\d*(\.\d*)?|0\.\d*[1-9]\d*$/,
        r_f_f: /^-([1-9]\d*(\.\d*)?|0\.\d*[1-9]\d*)$/,
        r_f: /^-?([1-9]\d*(\.\d*)?|0\.\d*[1-9]\d*|0?\.0+|0)$/,
        r_f_ff: /^[1-9]\d*(\.\d*)?|0\.\d*[1-9]\d*|0?\.0+|0$/,
        r_f_fz: /^(-([1-9]\d*(\.\d*)?|0\.\d*[1-9]\d*))|0?\.0+|0$/,
        _r_str: function (a) {
            return a.replace(/([+^$])/g, '\\' + '\$1');
        },
        m: function (a) {
            if (typeof a == 'number') return a;
            if (typeof a == 'boolean') return a ? 1 : 0;
            a = parseFloat(a);
            return isNaN(a) ? 0 : a;
        },
        m_scale: function (n, s, z) {
            var r = s.split(',');
            if (n < 0) {
                return js.map(r, 'v.indexOf("*")>-1||v.indexOf("%")>-1?-1:parseFloat(v)');
            }
            if (z == null) z = 0;
            var l = Math.max(n - js.sum(r, true), 0);
            var m = 0, k = -1;
            for (var i = r.length - 1, v; i > -1; i--) {
                v = r[i];
                if (v.indexOf('%') > -1) {
                    r[i] = Math.floor(l * parseFloat(v) / 100);
                    k = i;
                } else if (v.indexOf('*') > -1) {
                    m++;
                    if (v == '*') k = i;
                } else r[i] = parseFloat(v);
            }
            l = Math.max(n - js.sum(r, true), 0);
            for (var i = r.length - 1; i > -1; i--) {
                if (isNaN(r[i])) {
                    var a = Math.floor(l / m);
                    r[i] = r[i].indexOf('*') ? a - (a % parseFloat(r[i])) : (n == null ? n : a);
                }
                if (r[i] < z) r[i] = z;
            }
            l = Math.max(n - js.sum(r, true), 0);
            if (l > 0) r[k > -1 ? k : r.length - 1] += l;
            return r;
        },
        m_scalex: function (n, s, z) {
            if (s.indexOf('%') == -1 && s.indexOf('*') == -1) {
                var a = s.split(','), b = 0, c = 0;
                for (var i = 0; i < a.length; i++) b += (a[i] = parseFloat(a[i]));
                if (b != n) {
                    for (var i = 0; i < a.length; i++) {
                        a[i] = Math.floor(n * (a[i] / b));
                        c += a[i];
                    }
                    a[a.length - 1] += n - c;
                }
                return a;
            } else return this.m_scale(n, s, z);
        },
        m_scalegrid: function (n, s, z) {
            if (s.indexOf('%') == -1 && s.indexOf('*') == -1) {
                for (var i = 0, a = s.split(','), b = 0; i < a.length; i++) {
                    b += parseFloat(a[i]);
                }
                if (b < n) a[i - 1] = parseFloat(a[i - 1]) + n - b;
                return a;
            }
            return this.m_scalex(n, s, z);
        },
        m_add: function (a, b) {
            var c = js.s_fr(String(a), '.').length, d = js.s_fr(String(b), '.').length;
            if (c || d) {
                var e = Math.pow(10, Math.max(c, d));
                return Math.round(a * e + b * e) / e;
            }
            return a + b;
        },
        m_rng: function (a, b, c) {
            if (b != null && a < b) return b;
            if (c != null && a > c) return c;
            return a;
        },
        m_obi: function (a) {
            if (arguments.length > 1) {
                for (var i = 0, b; i < arguments.length; i++) {
                    b = arguments[i];
                    if (!isNaN(a[b])) a[b] = parseFloat(a[b]);
                }
            } else {
                for (var i in a) {
                    b = a[i];
                    if (!isNaN(b)) a[i] = parseFloat(a[i]);
                }
            }
            return a;
        },
        expr_split: function (a, b) {
            if (!b) b = ',';
            var d = a.split(new RegExp('\\s*' + this._r_str(b) + '\\s*', 'gi')), p = function (m, n) {
                m.replace(/([()\[\]])/g, function ($0, $1) {
                    n += $1 == '(' || $1 == '[' ? 1 : -1;
                });
                return n;
            };
            for (var i = 0, e, f, g; i < d.length; i++) {
                e = d[i];
                if (e.indexOf('(') > -1 || e.indexOf('[') > -1) {
                    f = p(e, 0);
                    g = i + 1;
                    while (f && d.length > g) {
                        f = p(d[g], f);
                        d[i] += b + d[g];
                        d.splice(g, 1);
                    }
                }
            }
            if (arguments.length > 2) {
                var e = js.argu(), f = 2, g;
                while (f < e.length && a.indexOf(e[f]) < 0) f++;
                if (g = e[f]) {
                    for (var i = d.length - 1; i > -1; i--) {
                        if (d[i] != g && d[i].indexOf(g) > -1) d.splice.apply(d, [i, 1, g].concat(arguments.callee.apply(this, [d[i]].concat(e.slice(f)))));
                    }
                }
            }
            return d;
        },
        _fps_que: {},
        fps_timer: function (a, b, c, d) {
            if (d) {
                clearInterval(this._fps_que[d]);
                delete this._fps_que[d];
            }
            a(0);
            if (b == null) b = 150;
            if (c == null) c = 15;
            var p = Math.ceil(b / c), cr = 0, d1 = new Date().getTime(), it = setInterval(function () {
                var d2 = new Date().getTime(), ri = d2 - d1, st = Math.floor(ri / p);
                if (cr != st) {
                    if (st < c) {
                        a((-Math.cos((st / c) * Math.PI) / 2) + 0.5);
                    } else {
                        clearInterval(it);
                        a(1);
                    }
                    cr = st;
                }
            }, Math.max(p - 10, 5));
            if (d) this._fps_que[d] = it;
            return it
        },
        hashArray: function () {
            return {
                _a: {}, add: function (a, b) {
                    if (this._a[a]) this._a[a].push(b); else this._a[a] = [b];
                }, get: function (a) {
                    return this._a[a];
                }
            }
        },
        cookie: function (a, b, c, d) {
            if (arguments.length === 1) return js.cookie_get(a); else js.cookie_set(a, b, c, d);
        },
        cookie_set: function (a, b, c, d) {
            var e = '';
            if (c) {
                var g = new Date();
                g.setTime(g.getTime() + (c * 24 * 60 * 60 * 1000));
                e = ';expires=' + g.toGMTString();
            }
            document.cookie = a + '=' + js.url_esc(b) + e + (d ? (';path=' + d) : '');
        },
        cookie_get: function (a) {
            var s = a + '=', c = document.cookie.split(';');
            for (var i = 0; i < c.length; i++) {
                var d = c[i];
                while (d.charAt(0) === ' ') d = d.substring(1, d.length);
                if (d.indexOf(s) === 0) return js.url_unesc(d.substring(s.length, d.length));
            }
            return null;
        },
        cookie_del: function (a) {
            this.cookie_set(a, '', -1);
        },
        dps: function (a) {
            for (var i = 1, b; i < arguments.length; i++) {
                b = arguments[i];
                if (b in a) {
                    a[b] = null;
                    delete a[b];
                }
            }
            delete a;
        },
        _addNode: Fn.Node.prototype.addNode,
        debug: function () {
            for (var i = 0, s = ''; i < arguments.length; i++) {
                s += arguments[i] + '\n'
            }
            alert(s)
        },
        fordebug: function (a, b) {
            var s = [];
            for (var i in a) {
                var t = typeof a[i];
                if ((b == 1 && t == 'function') || (b == 2 && (!t || (t != 'string' && t != 'number')))) continue;
                s.push(i + '=' + a[i]);
            }
            alert(s.join(b ? '; ' : '\n'));
        },
        toJSONString: function (a, b) {
            return a ? Fn.JS.JSON.stringify(a, b) : '';
        },
        parseJSON: function (a, b) {
            return a && Fn.JS.JSON.parse(a, b);
        },
        cloneJSON: function (a) {
            return this.parseJSON(typeof a == 'string' ? a : this.toJSONString(a));
        },
        printJSON: function (x, a) {
            return js.parseJSON(js[js.isArray(a) ? 's_printa' : 's_printx'](js.toJSONString(x), a));
        }
    }
});
Fn.JS.init = function () {
    window.js = new Fn.JS();
    window.$ = function (a, b) {
        return b ? b.all[a] : document.getElementById(a);
    };
    js.clone($, new Fn.Dom(window));
    if (!window.execScript) window.execScript = Function('a', 'this.eval(a)');
    window.Ajax = Fn._register_({
        Const: function (a, b) {
            this._src = a;
            if (b) this._data = b;
        }, Helper: {
            getreq: function () {
                return {req: Br.ajax_type === 1 ? new XMLHttpRequest() : new ActiveXObject(Br.ajax_type)};
            },
            _abort: function (a, b) {
                if (b && a.req.readyState <= 1) a._stop = b;
                try {
                    a.req.abort();
                } catch (ex) {
                }
                delete a.req.onreadystatechange;
                a.obj = a.loading = null;
            },
            _clean: function (a) {
            },
            cntp: 'application/x-www-form-urlencoded; charset=UTF-8',
            ifmod: 'Thu, 01 Jan 1970 00:00:00 GMT',
            _send: function (a, b, c, d, e, f, g, h, i, j, n, o) {
                if (Cfg.ajaxUrlEncode) a = Cfg.ajaxUrlEncode(a);
                if (a.indexOf('vm:|') === 0) a = a.substr(4);
                var u = a;
                if (c && typeof c === 'object') {
                    var s = [];
                    for (var k in c) s.push(k + '=' + js.url_esc(c[k]));
                    c = s.join('&');
                }
                if (n === 'get') {
                    if (c) u = a + (a.indexOf('?') > 0 ? '&' : '?') + c;
                } else if (n === 'post' || (a.length > 2000 && a.indexOf('?') > 0)) {
                    c = (c ? c + '&' : '') + js.s_fr(a, '?');
                    u = js.s_to(a, '?');
                    n = 'post';
                } else n = c ? 'post' : 'get';
                if (!c) c = null;
                if (f == null) f = Cfg.ajaxError;
                if (o == null) o = Cfg.ajaxTimeout;
                var k = Ajax.getreq(), l = k.req, t;
                if (d) k.sync = d;
                l.open(n, (u.charAt(0) == '/' || u.indexOf('http://') == 0 || u.indexOf('https://') == 0) ? u : Cfg.path + u, !d);
                if (Br.ie10) l.responseType = 'msxml-document';
                if (c) l.setRequestHeader('Content-Type', Ajax.cntp);
                l.setRequestHeader('x-requested-with', 'dfish');
                var uk;
                l.setRequestHeader('If-Modified-Since', Ajax.ifmod);

                function onchange() {
                    if (l.readyState == 4) {
                        if (o) clearTimeout(t);
                        var m = false, r = l.status === 500 && l.getResponseHeader('x-error') === 'dfish';
                        if (l.status < 400 || r) {
                            if (g == 1) {
                                if (l.responseXML.documentElement != null) {
                                    if ('setProperty' in l.responseXML) l.responseXML.setProperty('SelectionLanguage', 'XPath');
                                    m = l.responseXML.documentElement;
                                } else if (Br.local) {
                                    m = $.x_doc(l.responseText);
                                } else {
                                    if (Cfg.debug) {
                                        var ex = l.responseXML.parseError;
                                        debugger;
                                    }
                                }
                            } else {
                                m = l.responseText;
                                if (g == 2) {
                                    m = js.parseJSON(m);
                                }
                            }
                        }
                        if (m === false) Ajax._error(a, f, l, uk); else if (r && typeof f === 'function') f.apply(i, [l, a]);
                        if (b) {
                            if (j == null) b.call(i, m); else b.apply(i, [m].concat(j));
                        }
                        if (Br.ie || Br.fox) {
                            b = c = d = e = f = g = h = m = null;
                        }
                    }
                };l.onreadystatechange = onchange;
                var p = Ajax._paused;
                if (o) t = setTimeout(function () {
                    if (l.readyState !== 4) {
                        Ajax._abort(k);
                        Ajax._error(a, f, l, uk);
                    }
                }, o);
                if (d) {
                    l.send(c);
                } else {
                    setTimeout(function () {
                        if (k._stop) {
                            Ajax._abort(k);
                            k._stop = null;
                        } else {
                            if (!p && Ajax._paused) {
                                Ajax.addPause(function () {
                                    k.req.send(c)
                                });
                            } else k.req.send(c)
                        }
                    });
                }
                if (d && Br.fox) onchange();
                return k;
            },
            _paused: false,
            _pauseHdl: [],
            pause: function () {
                Ajax._paused = true;
            },
            addPause: function (a) {
                this._pauseHdl.push(a);
            },
            play: function () {
                for (var i = 0; i < this._pauseHdl.length; i++) {
                    setTimeout(this._pauseHdl[i], i * 10);
                }
                this._pauseHdl.length = 0;
                this._paused = false;
            },
            send: function (a, b, c, d, e, f) {
                this._send(a, b, null, e, true, f, null, null, c, d);
            },
            sendx: function (a, b, c, d, e, f) {
                this._send(a, b, null, e, true, f, 1, null, c, d);
            },
            text: function (a, b, c, d, e) {
                this._send(a, b, c, d, true, e);
            },
            xml: function (a, b, c, d, e) {
                this._send(a, b, c, d, true, e, 1);
            },
            json: function (a, b, c, d, e) {
                this._send(a, b, c, d, true, e, 2);
            },
            _error: function (a, b, c, uk) {
                if (typeof b === 'function') {
                    return b(c, a);
                }
                if (b !== true) {
                    if (Cfg.debug) {
                        var t = c.status, d = 'ajax error ' + t + ': ' + a + '\n\n', e = c.responseText;
                        if (t >= 500 && t <= 600) {
                            DFish.confirm(d + Loc.debug.view_more, function () {
                                $.wr(window.open(), e)
                            });
                        } else if (Loc.httpcode[t]) DFish.alert(d + Loc.httpcode[t] + (uk ? '\n\nuser key: ' + uk : '')); else alert(d + e);
                    } else DFish.alert(Loc.internet_error);
                }
            },
            scripts: {},
            loadjs: function (a, b, c, d, e) {
                if (b && typeof b != 'function') b = Fn.JS.F(b);
                if (typeof a == 'object') {
                    var f = a.length, k = 0;
                    if (f > 1) {
                        for (var i = 0; i < f; i++) {
                            Ajax.loadjs(a[i], function () {
                                for (var j = k, t; j < f && k < f; j++) {
                                    t = Ajax.scripts[a[j]];
                                    if (t && t.loaded) {
                                        k++;
                                        if (!t.used) window.execScript(t.text);
                                        t.loading = false;
                                        t.used = true;
                                        t.fireEvent('load');
                                        if (k == f) {
                                            k++;
                                            if (b) b();
                                        }
                                    } else {
                                        k = j;
                                        break;
                                    }
                                }
                            }, c, true);
                        }
                        return;
                    } else {
                        a = a[0];
                    }
                }
                if (Ajax.scripts[a] && Ajax.scripts[a].loaded && !Ajax.scripts[a].loading) {
                    if (b) b();
                    Ajax.scripts[a].fireEvent('load');
                    return;
                }
                if (!Ajax.scripts[a]) {
                    Ajax.scripts[a] = new Fn.Event();
                }
                if (Ajax.scripts[a].loading) {
                    if (b) Ajax.scripts[a].addEventOnce('load', b);
                    return;
                }
                Ajax.scripts[a].loading = true;
                Ajax.scripts[a].loaded = false;
                Ajax.send(js.url_set(a, 'v', Cfg.ver), function (s) {
                    if (s !== false) {
                        Ajax.scripts[a].text = s;
                        Ajax.scripts[a].loading = !!d;
                        Ajax.scripts[a].loaded = true;
                        if (!d) {
                            window.execScript(s);
                            Ajax.scripts[a].used = true;
                            Ajax.scripts[a].fireEvent('load');
                        }
                        if (b) {
                            b()
                        }
                    }
                }, null, null, c, e);
            },
            sends: function (a, b, c, d, e) {
                var f = a.length, g = new Array(f);
                for (var i = 0; i < a.length; i++) {
                    (function (k) {
                        Ajax.send(a[k], function (s) {
                            g[k] = s;
                            if (--f == 0) b.call(c, g);
                        }, c, d, e);
                    })(i);
                }
            },
            sendxs: function (a, b, c, d, e, f, ch) {
                var r = ch ? js.unique(a) : a, n = r.length, g = new Array(a.length), h = {};
                for (var i = 0; i < r.length; i++) {
                    (function (k) {
                        Ajax.sendx(r[k], function (s) {
                            if (f) h[r[k]] = s; else g[k] = s;
                            if (--n == 0) {
                                if (ch) {
                                    for (var j = 0; j < a.length; j++) g[j] = h[a[j]];
                                }
                                b.call(c, g);
                            }
                        }, c, d, e, f);
                    })(i);
                }
            },
            require: function (a, b) {
                this.loadjs(a, null, true, null, b);
            }
        }, Prototype: {
            _data: null, _hide: null, _xml: null, _obj: null, _sync: null, hide: function (a) {
                this._hide = a == null ? true : a;
                return this;
            }, sync: function () {
                this._sync = true;
                return this;
            }, bind: function (a) {
                this._obj = a;
                return this;
            }, send: function (a, b, c) {
                c = Ajax._send(this._src, a, this._data, this._sync, true, this._hide, null, this._obj, b, c);
                this._src = this._data = this._obj = null;
                return c;
            }, sendx: function (a, b, c) {
                c = Ajax._send(this._src, a, this._data, this._sync, true, this._hide, true, this._obj, b, c);
                this._src = this._data = this._obj = null;
                return c;
            }
        }
    });
    try {
        document.execCommand('BackgroundImageCache', false, true);
    } catch (e) {
    }
    if (!window.jQuery) {
        Ajax.require('js/jquery.min.js');
        window.Q = jQuery.noConflict();
    }
    Q.fn.extend({
        hoverClass: function (a) {
            Q(this).mouseover(function () {
                $.class_add(this, a);
            }).mouseout(function () {
                $.class_del(this, a);
            });
        }, setClass: function (a) {
            this.each(function () {
                this.className = a
            });
        }
    });
    if ((Br.gck || Br.opa)) Ajax.require('js/ieemu.js');
    var dl = !document.body;

    function _dwcss(a, b) {
        if (a.charAt(0) !== '/') a = Cfg.path + 'css/' + a;
        if (dl) document.write('<link rel="stylesheet" type="text/css" href="' + a + '"' + (b ? ' id="' + b + '"' : '') + '/>'); else $._css_import_link(a, null, null, b);
    };
    if (Br.ieVer) {
        var s = js.ids_add(document.documentElement.className, 'ie ie' + Br.ieVer, ' ');
        for (var i = Br.ieVer + 1; i < 12; i++) s += ' ltie' + i;
        document.documentElement.className = s;
    } else if (Br.mobile) {
        document.documentElement.className = js.ids_add(document.documentElement.className, 'mobile', ' ');
    }
    if (!Cfg.only_base) {
        _dwcss('global.css?v=' + Cfg.ver, 'fishcss-global');
        var k = Cfg.skin;
        _dwcss(k.theme + '/' + k.color + '/style.css', 'fishcss-color');
        if (Cfg.customGlobalCss) _dwcss(Cfg.customGlobalCss);
        if (Cfg.customSkinCss) _dwcss(Cfg.path + Cfg.customSkinCss, 'fishcss-customskin');
        if (Br._ie6) {
            _dwcss('global-ie6.css', 'fishcss-global-ie6');
        }
        k = location.protocol + '//' + location.host + Cfg.path + 'img/p/cur/';
        $.css_import_style('.f-pic-prev-cursor{cursor:url(' + k + 'pic_prev.cur),auto}.f-pic-next-cursor{cursor:url(' + k + 'pic_next.cur),auto}');
        if (dl) document.write(Fn.JS.jscr(true).join('\n'));
        var a = 0, onload = function () {
            if (++a == 1) VM.startByCfg();
        };
        if (Cfg.plus) {
            a = -1;
            document.addEventListener('plusready', function () {
                if (++a == 1) VM.startByCfg();
            }, false);
        }
        window.attachEvent('onload', onload);
        if (!Cfg.debug) window.onerror = function () {
            return !0
        }
    }
};
Fn.JS.jscr = function (a) {
    var b = 'loc/' + (Cfg.lang || 'zh_CN') + '.js',
        t = Cfg.php ? [b, 'fn/t/dialog.js', 'fn/t/menu.js', 'fn/div.js', 'fn/tree.js', 'fn/oper.js', 'fn/xbox.js', 'vm/vm.js', 'vm/lb.js', 'vm/mx.js', 'vm/sh.js'] : [b, 'main.js?v=' + Cfg.ver];
    for (var i = 0; i < t.length; i++) t[i] = (a ? '<script src="' : '') + Cfg.path + 'js/' + t[i] + (a ? '"></script>' : '');
    return t;
};
var DFish = {
    DISABLE: 'df.disable',
    BREAK: 'df.break',
    GET_VALUE: 'df.get_value',
    SET_VALUE: 'df.set_value',
    GET_TEXT: 'df.get_text',
    RESET: 'df.reset',
    FIRST: 'df.first',
    HIDDEN: 'df.hidden',
    XPATH: 'df.xpath',
    RESIZE: 'df.resize',
    WARN: 'df.warn',
    XML: 'df.xml',
    NOTNULL: 'df.notnull',
    READONLY: 'df.readonly',
    ADD_VALID: 'df.add_valid',
    MODIFY_VALID: 'df.modify_valid',
    REMOVE_VALID: 'df.remove_valid',
    TAB: 'df.tab',
    createClass: function (a) {
        return Fn.reg(a);
    },
    htc: {
        drags: [], g_pos: function (x, y) {
            var b = this._bs;
            for (var i = 0; i < b.length; i++) if (x > b[i].left && x < b[i].right && (y > b[i].top) && (y < b[i].bottom)) {
                if (this._index == i) return false;
                this._index = i;
                return this.drags[i];
            }
        }, on: function () {
            var a = [];
            for (var i = 0; i < this.drags.length; i++) {
                if (window[this.drags[i]]) a.push($.bcr(window[this.drags[i]])); else this.drags.splice(i--, 1);
            }
            this._bs = a;
            this._index = null;
        }, off: function () {
            this._bs = this._index = null;
            delete this._bs;
            delete this._index;
        }
    },
    debug: function (i, e, t) {
        var s = Loc.error + ': ';
        if (!Cfg.debug) return DFish.alert(s + '\n\n' + Loc.debug_sorry);
        switch (i) {
            case 1:
                s += Loc.debug_1 + ' "' + t + '"';
                break;
            case 2:
                s += Loc.debug_2;
                break;
            case 3:
                s += Loc.debug_3;
                break;
            case 4:
                s += Loc.ps(Loc.debug_4, t, e);
                return DFish.alert(s);
        }
        if (e) {
            s += '\n\ndescription: ' + e.description;
            if (t) s += '\n\nfrom: ' + js.s_trim(t);
            DFish.alert(s);
            throw e;
        } else DFish.alert(s);
    },
    alert: function (a, b, c, d, e, f) {
        var g = a;
        if (e == null) e = window.VM ? VM() : (parent && parent.VM && parent.VM());
        if (typeof a === 'object') {
            if (a.length) g = a[0].msg || a[0]; else return;
        }
        if (e && e.vis) e.cmd({tagName: 'alert', cn: '' + g, pos: b || 0, tm: c || 0, onclose: d, n: f}); else {
            alert(g);
            if (d) d();
        }
    },
    confirm: function (a, b, c, ft) {
        var vm = VM();
        if (vm.vis) {
            VM().exec({tagName: 'confirm', cn: a, fnYes: b, fnNo: c, fnType: 1}, null, ft);
        } else {
            if (confirm(a)) {
                if (b) b();
            } else if (c) c();
        }
    },
    download: function (a) {
        var b = this.download._cnt, c = '_dfish-dlfr-' + (b || 0);
        if (!$(c)) $.db($.htm.hiframe(c));
        frames[c].location.replace(a);
        if ((this.download._cnt = (b || 0) + 1) > 3) this.download._cnt = 0;
    },
    g_dialog: function (a) {
        return Fn.Dialog.get(a.$ ? a.$() : a);
    },
    close: function (a) {
        var b = this.g_dialog(a);
        if (b) b.close();
    },
    completeInputBox: function (a, v) {
        var b = VM(a).ext('xboxid'), c = b && Fn.XBox.getById(b);
        if (c) {
            c._complete(a);
        } else if (v) {
            b = DFish.g_dialog(a).args();
            if (b) {
                VM.MX.f_focus(b.ipt);
                b.ipt.focus();
                if (Br.ie) {
                    b.sel.text = '';
                    b.sel.collapse(false);
                    b.sel.select();
                    b.sel.text = v;
                } else {
                    var o = b.ipt, p = o.selectionStart, s = o.value;
                    o.value = s.substring(0, p) + v + s.substring(o.selectionEnd, s.length);
                    o.selectionStart = o.selectionEnd = p + v.length;
                }
            }
        }
    },
    batchBox: function (o, grid, tit, bar, bn) {
        var vm = VM(o), grid = vm.find(grid), tit = vm.find(tit), bar = vm.find(bar), bn = '_batch',
            e = $.get('input[name=' + bn + ']', vm.$());
        if (o.name === bn) {
            grid.checkAll(o.checked);
        } else {
            var c = $(grid.id + '-abx'), d = grid.getBox().length, g = !!(d && !grid.getBox(false).length);
            if (e) e.checked = g;
            if (c && c !== o) c.checked = g;
        }
        var d = grid.getBox(true).length, f = '_' + tit.frid + '-bx';
        if (!vm.find(f)) vm.cmd('<insert type="panel" target="' + tit.frid + '" where="afterend"><fr type="html" id="' + f + '" class="f-12" style="margin:0 8px 0 -12px;line-height:' + tit.dv().ht + 'px;"></fr></insert>');
        vm.find(f).html(d ? '<div style=font-size:14px;padding-left:10px>' + Loc.ps(Loc.choose_already, d) + '</div>' : '');
        vm.find(f).$().style.marginLeft = e ? '-12px' : 0;
        $.toggle(tit.$(), !d);
        $.toggle(bar.$(), !!d);
    },
    loadView: function (a, b, c, d) {
        return typeof a === 'string' ? VM.go(VM.p_film(new Fn.DivSet('rows=*', c, d).render(b)), a) : VM.xgo(new Fn.DivFilm(c, d).render(b), a);
    },
    snapDiv: function (a, b, w, h, o) {
        var r = $.win(o).$.snaprec(w, h, o),
            s = 'position:absolute;width:' + w + 'px;top:' + r.top + 'px;left:' + r.left + 'px;', t = '';
        if (Br.ie6c()) t += $.htm.ie6fr(w, h + 2, '', a + '-ie6fr', s);
        t += '<div id=' + a + ' class="' + (b || '') + '" style="' + s + 'z-index:' + $.g_z() + ';overflow:hidden;"></div>';
        $.db(t);
        return $(a);
    },
    tip: function (x) {
        return x.nodeType ? Fn.Tip.get(x) : Fn.Tip.create(x);
    },
    url: function (a) {
        return a ? (a.indexOf('/') == 0 || a.indexOf('http://') == 0 || a.indexOf('https://') == 0 ? '' : Cfg.path) + a : '';
    },
    print: function (a, z) {
        var b = $.tags('link'), c = [];
        for (var i = 0; i < b.length; i++) {
            c.push(b[i].outerHTML);
        }
        c.push('<style>.text{border:0;}.xboxic,.btn{display:none}div{overflow:hidden!important;}span{display:inline!important}</style>');
        var d = a.$ ? a.$() : a, s = d.outerHTML;
        var e = $.tags('select', d);
        for (var i = e.length - 1; i > -1; i--) {
            var f = e[i], g = f.length > 0 ? f[f.selectedIndex] : null;
            s = s.replace(f.outerHTML, '<div align=center>' + (g ? g.text : '') + '</div>');
        }
        var e = $.tags('input', d);
        for (var i = e.length - 1; i > -1; i--) {
            var f = e[i];
            if (f.type != 'hidden') s = s.replace(f.outerHTML, '<div align=center>' + f.value + '</div>');
        }
        var e = $.tags('textarea', d);
        for (var i = e.length - 1; i > -1; i--) {
            var f = e[i];
            s = s.replace(f.outerHTML, '<div align=center>' + f.value + '</div>');
        }
        s = s.replace(/<div[^>]+overflow-y[^>]+>/gi, function ($0) {
            return $0.replace(/height: \w+/gi, '');
        });
        d = window.open();
        $.wr(d, $.htm.cmptag('<title>' + Loc.print_preview + '</title>' + c.join('')) + '<body>' + s + '</body></html>');
        if (z) d.print();
        return d;
    },
    _viroper: {},
    btn: function (a, b) {
        if (typeof a === 'string') {
            a = $.x_json($.x_doc(a));
        } else if (a.nodeType) {
            a = $.x_json(a);
        }
        if (!b) {
            b = {func: 'office', face: 'o-office', cellspacing: 0, valign: 'middle'};
        }
        var o = (this._viroper[b.face] || (this._viroper[b.face] = new Fn.Oper(b))).add(a);
        return o;
    },
    oper: function (a, b) {
        if (!a) a = {func: 'classic', face: 'o-classic-s'};
        var c = new Fn.Oper(a);
        if (b) {
            for (var i = 0; i < b.length; i++) c.add(b[i]);
        }
        return c;
    },
    skin: function (a, b, c) {
        b = b || Cfg.skin;
        if (a.bg != b.bg) {
            $.class_del(document.body, b.bg);
            $.class_add(document.body, a.bg);
        }
        if (a.theme != b.theme || a.color != b.color) {
            $.rm('fishcss-color');
            var l = $._css_create_link('css/' + a.theme + '/' + a.color + '/style.css', null, null, 'fishcss-color');
            $.after($('fishcss-global'), l);
        }
        if (a.theme != b.theme) {
            VM().reload(js.url_set(c || ('vm:|' + VM().src), 'theme', a.theme));
            return true;
        }
    },
    delay: function (fn, delay, exclusion) {
        var timerID;
        return function () {
            if (exclusion) {
                clearTimeout(timerID);
            }
            timerID = setTimeout(fn, delay);
        };
    },
    collpaseLeftPanel: function (a, b, z) {
        if (!a.onclick) {
            var c = a, d, e;
            while (c && !(d = Fn.Div._a[c.id])) c = c.parentNode;
            if (d) {
                while (d && !d._b_valve) d = d.parentNode;
                if (d) {
                    var z = z || 'l', e = d.nodeIndex, p = d.parentNode, t = z == 'l' || z == 't' ? p[e - 1] : p[e + 1],
                        s = b || t.wd;
                    a.onclick = function () {
                        if (!t.wd) t.$().style.display = '';
                        Fn.DivSet.resep(p, z == 'l' || z == 't' ? (t.wd ? 0 : s) + ',' + p.scales[1] + ',*' : '*,' + p.scales[1] + ',' + (t.wd ? 0 : s));
                        d.fireEvent('valve');
                    };
                    d.addEvent('valve', function () {
                        t.$().style.display = t.wd ? '' : 'none';
                        a.className = t.wd ? 'bg-clp-' + z : 'bg-clp-' + (z == 'l' ? 'r' : (z == 'r' ? 'l' : (z == 't' ? 'b' : 't')));
                    });
                }
            }
        }
        if ((!window.event || !a.contains(event.srcElement)) && a.onclick) a.onclick();
        $.e_stop();
    },
    collpaseRightPanel: function (a, b) {
        this.collpaseLeftPanel(a, b, 'r');
    },
    collpaseTopPanel: function (a, b) {
        this.collpaseLeftPanel(a, b, 't');
    },
    collpaseBottomPanel: function (a, b) {
        this.collpaseRightPanel(a, b, 'b');
    },
    collapseParent: function (a, b, c, d) {
        if (b == null && a._collapseSize == null) return;
        var v = VM(a), f = v.parent._dvs[v.pkid], p;
        while (f && (p = f.parentNode) && !(p.type === (d ? 'rows' : 'cols') && p.scales.length > 1)) f = p;
        if (p) {
            if (typeof b === 'object') {
                var e = a.attr('ic') == b[1];
                Fn.Div.resizeTo(f, v._collapseSize = e ? (v._collapseMaxSize || c[0]) : b[0]);
                a.attr('ic', e ? c[1] : b[1]);
            } else {
                if (b != null) {
                    if (typeof b === 'string') {
                        if (b.charAt(1) === '=') {
                            var e = d ? f.ht : f.wd;
                            eval('e' + b);
                            b = e;
                        } else b = parseFloat(b);
                    }
                    v._collapseSize = v._collapseMaxSize = b;
                }
                Fn.Div.resizeTo(f, v._collapseSize);
            }
        }
    },
    contentToggle: function (a, b, c) {
        VM.LB.contentToggle(a, b, null, c);
    },
    sleep: function (a, b) {
        var f = 'VM().cmd({tagName:"ajax",src:"' + a + '"});DFish.sleepHandler();';
        (DFish.sleepHandler = function (e) {
            clearTimeout(DFish._sleepTimer);
            DFish._sleepTimer = setTimeout(f, b * 1000);
        })();
        DFish.attachSleep($.dc());
    },
    attachSleep: function (a) {
        if (DFish.sleepHandler) $.e_add(a, 'mousedown keydown mouseover', DFish.sleepHandler);
    },
    drag: function (a, b) {
        var dr = {
            $: function () {
                return js.type(a) == 'html' ? a : a.$()
            }, to: function (f) {
                this._to = f;
                if (js.type(a) == 'html') this.moveLink(event);
            }, moveLink: function (e) {
                var r = $.bcr(a), _x = e.clientX, _y = e.clientY, s = js.s_trim(a.innerText);
                if (!s) {
                    var img = $.get('img', a);
                    if (img) s = '<img style="max-width:32px;max-height:32px;" src=' + img.src + '>';
                }
                var c = $.db('<div style="position:absolute;left:' + (_x + 20) + 'px;top:' + (_y + 6) + 'px;border:1px dashed gray;padding:0 5px;max-width:200px;background:#fafafa;cursor:default;z-index:999;color:gray;" class="fix opc8">' + s + '</div>');
                dr.on(a, c);
                var f;
                $.e_moveup(c, function (e) {
                    var x = e.clientX, y = e.clientY;
                    c.style.posLeft = x + 18;
                    c.style.posTop = y + 6;
                    if (dr.onmove) {
                        dr.onmove(x, y, e.srcElement);
                    }
                    if (dr.getTarget && (f = dr.getTarget())) {
                        if (f != dr.target) {
                            dr.target = f;
                            dr.target.s_drag(dr.src, dr.sortSrc);
                            dr.target.drag.xdrag(a, c, dr.src, dr.sortSrc);
                        }
                    }
                }, function () {
                    $.rm(c)
                });
            }, on: function (x, o) {
                dr._to();
                if (dr.onmove) {
                    if (a.type === 'sh') {
                        $.e_moveup(x, function (e) {
                            if (dr.onmove) {
                                dr.onmove(e.clientX, e.clientY, e.srcElement);
                            }
                            if (!dr.target && dr.getTarget && (f = dr.getTarget())) {
                                dr.target = f;
                                dr.target.s_drag(dr.src, dr.sortSrc);
                                dr.target.drag.xdrag(x, o, dr.src, dr.sortSrc);
                            }
                        }, function () {
                            dr.target = null
                        });
                    }
                }
            }
        };
        if (b) dr.to(b);
        if (js.type(a) === 'html') {
        } else {
            a.addEvent('dragstart', dr.on);
            if (a.type == 'lb' && !a.aZ) $(a.id + '-tb').onmousedown = function () {
                VM.LB._dragstart(this.id)
            }; else if (a.type === 'sh') {
                if (!a.x('pub/drag')) {
                    a.s_drag();
                    a.$().onmousedown = function () {
                        a.drag_down(event)
                    };
                    a.$().ondragstart = function () {
                        a.drag.start(event)
                    }
                }
                if (a.drag._from_node) a.drag.fire();
            } else if (a.type === 'img') {
                dr._to();
            }
        }
        return dr;
    },
    thumbnail: (function () {
        function _thumb(img, maxWidth, fn) {
            var w = img.width;
            if (w === 0) {
                var o = new Image();
                o.src = img.src;
                w = o.width;
            }
            if (w > maxWidth) {
                img.style.maxWidth = maxWidth + 'px';
                img.style.height = 'auto';
                img.removeAttribute('height');
                if (!img.title) img.title = Loc.click_preview;
                if (fn !== false) {
                    $.class_add(img, 'hand');
                    $.class_add(img, 'f-thumbnail');
                }
                if (fn && img.parentNode.tagName !== 'A') {
                    if (typeof fn === 'string') {
                        Q(img).wrap('<a href="' + js.s_printa(fn, [img.src, img.alt || img.title], true) + '" target=_blank></a>');
                    } else if (typeof fn === 'function') {
                        Q(img).click(fn);
                    }
                }
            }
        };

        function _prev($img, g) {
            var n = js.index($img, g);
            return n > 0 ? $img[n - 1] : null;
        };

        function _next($img, g) {
            var n = js.index($img, g);
            return n < $img.length - 1 ? $img[n + 1] : null;
        };

        function _btn(a, p, n, k) {
            if (Br.ie) {
                a.$().style.cursor = 'auto';
            }
            var vis = p ? 'visible' : 'hidden';
            if (vis !== $('f:thumbnail-prev').style.visibility) {
                $('f:thumbnail-prev').style.visibility = vis;
            }
            vis = n ? 'visible' : 'hidden';
            if (vis !== $('f:thumbnail-next').style.visibility) {
                $('f:thumbnail-next').style.visibility = vis;
            }
            $('f:thumbnail-page').innerText = k + 1;
        };

        function _pop($img, g) {
            var c = $.canrec(), w = c.width * .9;
            h = c.height - 60, p = _prev($img, g), n = _next($img, g), k = js.index($img, g), a = new Fn.Div(w, h, '', 'background:#000;'), s = '<table cellspacing=0 cellpadding=0 width=100% height=100%><tr><td align=center valign=middle><img id=f:thumbnail-img src=' + g.src + ' style="max-width:' + (w - 40) + 'px;max-height:' + (h - 50) + 'px;"></table><div style="position:absolute;bottom:2px;left:50%;color:#fff"><span id=f:thumbnail-page>' + (k + 1) + '</span>/' + $img.length + '</div><div class="opc0 ps-abs f-pic-prev-cursor" id=f:thumbnail-prev style="visibility:' + (p ? 'visible' : 'hidden') + ';top:0;left:0;bottom:0;background:#000;width:' + (w / 2) + 'px"></div><div class="opc0 ps-abs f-pic-next-cursor" id=f:thumbnail-next style="visibility:' + (n ? 'visible' : 'hidden') + ';top:0;right:0;bottom:0;background:#000;width:' + (w / 2) + 'px"></div>' + $.htm.sic('.i-x-preview .hand', 'position:absolute;right:-18px;top:-18px', '', 'DFish.close(this);$.e_stop()');
            a.data(s);
            var d = new Fn.Dialog({
                tagName: 'dialog',
                w: w,
                h: h,
                cvr: 1,
                cvr_style: 'background:#000;opacity:.75;filter:Alpha(opacity=75)'
            }, a).render();
            $('f:thumbnail-prev').onclick = function () {
                $('f:thumbnail-img').src = p.src;
                n = _next($img, p);
                p = _prev($img, p);
                _btn(a, p, n, --k);
            };
            $('f:thumbnail-next').onclick = function () {
                $('f:thumbnail-img').src = n.src;
                p = _prev($img, n);
                n = _next($img, n);
                _btn(a, p, n, ++k);
            }
        };
        return function (range, maxWidth, fn) {
            var $img = Q('img', range);
            $img.each(function () {
                if (this.complete) {
                    _thumb(this, maxWidth, fn);
                } else {
                    Q(this).on('load', function () {
                        _thumb(this, maxWidth, fn);
                    });
                }
            });
            if (fn == null || fn === true) {
                Q(range).click(function (ev) {
                    if ($.class_any(ev.target.className, 'f-thumbnail')) {
                        _pop($img, ev.target);
                    }
                });
            }
        }
    })(),
    swf: function (a, b, c, d, e) {
        var _SWFObject = function (_1, id, w, h, _5, c, _7, _8, _9, _a) {
            this._2e = _getRequestParameter(_a || "detectflash");
            this._params = {};
            this._variables = {};
            this._attributes = [];
            if (_1) {
                this._setAttribute("swf", _1);
            }
            if (id) {
                this._setAttribute("id", id);
            }
            if (w) {
                this._setAttribute("width", w);
            }
            if (h) {
                this._setAttribute("height", h);
            }
            if (_5) {
                this._setAttribute("version", new _PlayerVersion(_5.toString().split(".")));
            }
            this._installedVer = _getPlayerVersion();
            if (c) {
                this.addParam("bgcolor", c);
            }
            this.addParam("quality", _7 || "high");
            this._setAttribute("xiRedirectUrl", _8 || location);
            this._setAttribute("redirectUrl", "");
            if (_9) {
                this._setAttribute("redirectUrl", _9);
            }
        };
        _SWFObject.prototype = {
            _setAttribute: function (_e, _f) {
                this._attributes[_e] = _f;
            }, _getAttribute: function (_10) {
                return this._attributes[_10];
            }, addParam: function (_11, _12) {
                this._params[_11] = _12;
            }, _getParams: function () {
                return this._params;
            }, addVariable: function (_13, _14) {
                this._variables[_13] = _14;
            }, getVariable: function (_15) {
                return this._variables[_15];
            }, _getVariablePairs: function () {
                var _16 = [];
                var key;
                var _18 = this._variables;
                for (key in _18) {
                    _16[_16.length] = key + "=" + js.s_quot(_18[key]);
                }
                return _16;
            }, getSWFHTML: function () {
                var _19 = "";
                if (navigator.plugins && navigator.mimeTypes && navigator.mimeTypes.length) {
                    if (this._getAttribute("doExpressInstall")) {
                        this.addVariable("MMplayerType", "PlugIn");
                        this._setAttribute("swf", this._xiSWFPath);
                    }
                    _19 = '<embed type="application/x-shockwave-flash" src="' + this._getAttribute("swf") + '" width="' + this._getAttribute("width") + '" height="' + this._getAttribute("height") + '" style="' + this._getAttribute("style") + '" id="' + this._getAttribute("id") + '" name="' + this._getAttribute("id") + '" ';
                    var _1a = this._getParams();
                    for (var key in _1a) {
                        _19 += [key] + '="' + _1a[key] + '" ';
                    }
                    var _1c = this._getVariablePairs().join("&");
                    if (_1c.length > 0) {
                        _19 += 'flashvars="' + _1c + '"';
                    }
                    _19 += "/>";
                } else {
                    if (this._getAttribute("doExpressInstall")) {
                        this.addVariable("MMplayerType", "ActiveX");
                        this._setAttribute("swf", this._xiSWFPath);
                    }
                    var h = this._getAttribute("id"), k = this._getAttribute("style");
                    _19 = '<object' + (h ? ' id="' + h + '"' : '') + ' classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="' + this._getAttribute("width") + '" height="' + this._getAttribute("height") + '"' + (k ? ' style="' + k + '"' : '') + '><param name="movie" value="' + this._getAttribute("swf") + '" />';
                    var _1d = this._getParams();
                    for (var key in _1d) {
                        _19 += '<param name="' + key + '" value="' + _1d[key] + '" />';
                    }
                    var _1f = this._getVariablePairs().join("&");
                    if (_1f.length > 0) {
                        _19 += '<param name="flashvars" value="' + _1f + '" />';
                    }
                    _19 += "</object>";
                }
                return _19;
            }
        };

        function _getPlayerVersion() {
            var _23 = new _PlayerVersion([0, 0, 0]);
            if (navigator.plugins && navigator.mimeTypes.length) {
                var x = navigator.plugins["Shockwave Flash"];
                if (x && x.description) {
                    _23 = new _PlayerVersion(x.description.replace(/([a-zA-Z]|\s)+/, "").replace(/(\s+r|\s+b[0-9]+)/, ".").split("."));
                }
            } else {
                if (navigator.userAgent && navigator.userAgent.indexOf("Windows CE") >= 0) {
                    var axo = 1;
                    var _26 = 3;
                    while (axo) {
                        try {
                            _26++;
                            axo = new ActiveXObject("ShockwaveFlash.ShockwaveFlash." + _26);
                            _23 = new _PlayerVersion([_26, 0, 0]);
                        } catch (e) {
                            axo = null;
                        }
                    }
                } else {
                    try {
                        var axo = new ActiveXObject("ShockwaveFlash.ShockwaveFlash.7");
                    } catch (e) {
                        try {
                            var axo = new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6");
                            _23 = new _PlayerVersion([6, 0, 21]);
                            axo.AllowScriptAccess = "always";
                        } catch (e) {
                            if (_23.major == 6) {
                                return _23;
                            }
                        }
                        try {
                            axo = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
                        } catch (e) {
                        }
                    }
                    if (axo != null) {
                        _23 = new _PlayerVersion(axo.GetVariable("$version").split(" ")[1].split(","));
                    }
                }
            }
            return _23;
        };

        function _PlayerVersion(_29) {
            this.major = _29[0] != null ? parseInt(_29[0]) : 0;
            this.minor = _29[1] != null ? parseInt(_29[1]) : 0;
            this.rev = _29[2] != null ? parseInt(_29[2]) : 0;
        };

        function _getRequestParameter(_2b) {
            var q = location.search || location.hash;
            if (_2b == null) {
                return q;
            }
            if (q) {
                var _2d = q.substring(1).split("&");
                for (var i = 0; i < _2d.length; i++) {
                    if (_2d[i].substring(0, _2d[i].indexOf("=")) == _2b) {
                        return _2d[i].substring((_2d[i].indexOf("=") + 1));
                    }
                }
            }
            return "";
        };
        return new _SWFObject(a, b, c, d, e);
    }
};
Fn.JS.init();