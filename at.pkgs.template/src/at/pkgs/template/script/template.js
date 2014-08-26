(function(root, unasigned) {
	at.pkgs.TemplateEngine.instance.pattern =
		/\[%([\s\S]*?)(%?)%\]|\[=([\s\S]*?)(=?)=\]|\[@([\s\S]*?)(@?)@\]|$/g;
	root.template = function(source) {
		return {
			template: at.pkgs.template(source),
			render: function(attributes) {
				var context;
				var iterator;
				var key;

				context = new Object();
				iterator = attributes.keySet().iterator();
				while (iterator.hasNext()) {
					key = iterator.next();
					context[key] = attributes.get(key);
				}
				return this.template(context);
			}
		};
	};
})(this);
