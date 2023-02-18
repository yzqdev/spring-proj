using System;
using System.Collections.Generic;

namespace Yi.Framework.Common.IOCOptions
{
	public class SqlConnOptions
	{
		public string WriteUrl { get; set; } = string.Empty;
		public List<string>? ReadUrl { get; set; }
	}
}
