(function(root, unasigned) {
	at.pkgs.TemplateEngine.instance.pattern =
		/\[%([\s\S]*?)(%?)%\]|\[=([\s\S]*?)(=?)=\]|\[@([\s\S]*?)(@?)@\]|$/g;
	root.template = function(source) {
		return {
			template: at.pkgs.template(source),
			render: function(iterable) {
				var context;
				var iterator;
				var entry;

				context = new Object();
				iterator = iterable.iterator();
				while (iterator.hasNext()) {
					entry = iterator.next();
					context[entry.getKey()] = entry.getValue();
				}
				return this.template(context);
			}
		};
	};
})(this);
