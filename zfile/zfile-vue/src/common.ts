const fileTypeMap: any = {
  image: ["gif", "jpg", "jpeg", "png", "bmp", "webp", "ico"],
  video: ["mp4", "webm", "m3u8", "rmvb", "avi", "swf", "3gp", "mkv", "flv"],
  audio: ["mp3", "wav", "wma", "ogg", "aac", "flac", "m4a"],
  text: [
    "css",
    "js",
    "jsx",
    "ts",
    "tsx",
    "json",
    "md",
    "xml",
    "yml",
    "yaml",
    "toml",
    "txt",
    "py",
    "go",
    "html",
    "less",
    "php",
    "rb",
    "rust",
    "script",
    "java",
    "vue",
    "log",
    "sh",
  ],
  executable: ["exe", "dll", "com", "vbs"],
  archive: ["7z", "zip", "rar", "tar", "gz"],
  document: [
    "doc",
    "txt",
    "docx",
    "pages",
    "epub",
    "pdf",
    "numbers",
    "csv",
    "xls",
    "xlsx",
    "keynote",
    "ppt",
    "pptx",
  ],
};

import config from "../package.json";

const iconFileType = [
  ...new Set([
    "css",
    "go",
    "html",
    "js",
    "less",
    "php",
    "py",
    "rb",
    "rust",
    "script",
    "map",
    "lock",
    "md",
    "apk",
    "deb",
    "rpm",
    "java",
    "docx",
    "xlsx",
    "pptx",
    "ppt",
    "js",
    "jsx",
    "ts",
    "tsx",
    "json",
    "md",
    "xml",
    "yml",
    "yaml",
    "txt",
    "mp4",
    "mkv",
    "psd",
    "py",
    "go",
    "html",
    "less",
    "php",
    "rb",
    "sql",
    "exe",
    "rust",
    "script",
    "java",
    "vue",
    "log",
    "sh",
  ]),
];

let common = {
  responseCode: {
    SUCCESS: 0,
    FAIL: -1,
    REQUIRED_PASSWORD: -2,
    INVALID_PASSWORD: -3,
  },
  config: config,
  version: config.version,
  constant: {
    fileTypeMap,
    iconFileType,
  },
  fileSizeFormat: (bytes: number) => {
    if (bytes === 0) return "0 B";
    let k = 1024;
    let sizes = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
    let i = Math.floor(Math.log(bytes) / Math.log(k));
    return (bytes / Math.pow(k, i)).toFixed(2) + " " + sizes[i];
  },
  fileSizeFilter: (row: any, column: any, bytes: number) => {
    if (row.type === "BACK") return "";
    if (row.type === "FOLDER") return "-";
    if (bytes === 0) return "0 B";
    let k = 1024;
    let sizes = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
    let i = Math.floor(Math.log(bytes) / Math.log(k));
    return common.fileSizeFormat(bytes);
  },
  getFileIconName(file: File) {
    let ICON_PREFIX = "icon-";
    let iconName;
    if (file.type === "BACK" || file.type === "FOLDER") {
      return ICON_PREFIX + file.type.toLowerCase();
    } else {
      let fileSuffix = this.getFileSuffix(file.name);
      let fileType = this.getFileType(file.name);

      if (iconFileType.includes(fileSuffix)) {
        iconName = ICON_PREFIX + fileSuffix;
      } else if (fileType) {
        iconName = ICON_PREFIX + fileType;
      } else {
        iconName = ICON_PREFIX + "file";
      }
    }
    return iconName;
  },
  getFileSuffix(name: string) {
    let lastIndex = name.lastIndexOf(".");
    if (lastIndex === -1) {
      return "other";
    }
    return name.substring(lastIndex + 1).toLowerCase();
  },
  getFileType(name: string) {
    let fileType;
    for (let key in fileTypeMap) {
      let suffix = this.getFileSuffix(name);
      if (fileTypeMap[key].indexOf(suffix) !== -1) {
        fileType = key;
        break;
      }
    }
    return fileType;
  },
  removeDuplicateSeparator(path: string) {
    let result = "";

    if (path.indexOf("http://") === 0) {
      result = "http://";
    } else if (path.indexOf("https://") === 0) {
      result = "https://";
    }

    for (let i = result.length; i < path.length - 1; i++) {
      let current = path.charAt(i);
      let next = path.charAt(i + 1);
      if (!(current === "/" && next === "/")) {
        result += current;
      }
    }
    result += path.charAt(path.length - 1);
    return result;
  },
  isMobile() {
    let flag = navigator.userAgent.match(
      /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
    );
    return flag || window.innerWidth < 768;
  },
  dateFormat: function (time: string) {
    let date = new Date(time);
    let year = date.getFullYear();
    let month =
      date.getMonth() + 1 < 10
        ? "0" + (date.getMonth() + 1)
        : date.getMonth() + 1;
    let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    let minutes =
      date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    let seconds =
      date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  },
};

export default common;
