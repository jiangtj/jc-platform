(function(global, factory) {
  typeof exports === "object" && typeof module !== "undefined" ? factory(require("vue")) : typeof define === "function" && define.amd ? define(["vue"], factory) : (global = typeof globalThis !== "undefined" ? globalThis : global || self, factory(global.Vue));
})(this, function(vue) {
  "use strict";var __async = (__this, __arguments, generator) => {
  return new Promise((resolve, reject) => {
    var fulfilled = (value) => {
      try {
        step(generator.next(value));
      } catch (e) {
        reject(e);
      }
    };
    var rejected = (value) => {
      try {
        step(generator.throw(value));
      } catch (e) {
        reject(e);
      }
    };
    var step = (x) => x.done ? resolve(x.value) : Promise.resolve(x.value).then(fulfilled, rejected);
    step((generator = generator.apply(__this, __arguments)).next());
  });
};

  const role_vue_vue_type_style_index_0_scoped_ee5dfb38_lang = "";
  const _export_sfc = (sfc, props) => {
    const target = sfc.__vccOpts || sfc;
    for (const [key, val] of props) {
      target[key] = val;
    }
    return target;
  };
  const _sfc_main = {
    props: {
      instance: {
        type: Object,
        required: true
      }
    },
    data: () => ({
      roles: [],
      roleInfo: ""
    }),
    created() {
      return __async(this, null, function* () {
        const response = yield this.instance.axios.get("actuator/role");
        this.roles = response.data;
      });
    },
    mounted() {
    },
    methods: {
      showRoleInfo(name) {
        return __async(this, null, function* () {
          const response = yield this.instance.axios.get("actuator/role/" + name);
          this.roleInfo = response.data;
        });
      }
    }
  };
  const _withScopeId = (n) => (vue.pushScopeId("data-v-ee5dfb38"), n = n(), vue.popScopeId(), n);
  const _hoisted_1 = { class: "custom" };
  const _hoisted_2 = ["textContent"];
  const _hoisted_3 = /* @__PURE__ */ _withScopeId(() => /* @__PURE__ */ vue.createElementVNode("p", null, "Roles:", -1));
  const _hoisted_4 = ["onClick"];
  const _hoisted_5 = ["textContent"];
  function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("div", _hoisted_1, [
      vue.createElementVNode("p", null, [
        vue.createTextVNode("Instance: "),
        vue.createElementVNode("span", {
          textContent: vue.toDisplayString($props.instance.id)
        }, null, 8, _hoisted_2)
      ]),
      _hoisted_3,
      vue.createElementVNode("p", null, [
        (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(_ctx.roles, (item) => {
          return vue.openBlock(), vue.createElementBlock("li", {
            key: item,
            onClick: ($event) => $options.showRoleInfo(item)
          }, vue.toDisplayString(item), 9, _hoisted_4);
        }), 128))
      ]),
      vue.createElementVNode("p", null, [
        vue.createElementVNode("span", {
          textContent: vue.toDisplayString(_ctx.roleInfo)
        }, null, 8, _hoisted_5)
      ])
    ]);
  }
  const roleComponent = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-ee5dfb38"]]);
  SBA.viewRegistry.addView({
    name: "instances/role",
    parent: "instances",
    path: "role",
    component: roleComponent,
    label: "Role",
    group: "role",
    // icon: roleIcon,
    order: 1e3,
    isEnabled: ({ instance }) => instance.hasEndpoint("role")
  });
});
//# sourceMappingURL=custom-ui.js.map
