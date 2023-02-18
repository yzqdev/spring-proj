using System;

namespace Yi.Framework.Common.Base
{
    #region 无值定义

    /// <summary>
    /// 无值定义
    /// </summary>
    public class NullValue
    {
        /// <summary>
        /// 唯一识别型的默认无值
        /// </summary>
        public static readonly Guid Guid = Guid.Empty;

        /// <summary>
        /// 日期时间的默认无值
        /// </summary>
        /// <remarks>解决C#DateTime最小值是SQL 2005不允许的范围内</remarks>
        public static readonly DateTime DateTime = DateTime.Parse("1753-1-1 12:00:01");

        /// <summary>
        /// JavaScript日期时间的默认无值
        /// </summary>
        /// <remarks></remarks>
        public static readonly DateTime JavaScriptDateTime = DateTime.Parse("1970-1-1 00:00:00");

        #region 数字类型

        /// <summary>
        /// 短整数的默认无值
        /// </summary>
        public const short ShortInterger = -1;//short.MinValue;

        /// <summary>
        /// 整数的默认无值
        /// </summary>
        public const int Interger = -1;//int.MinValue;

        /// <summary>
        /// 长整数的默认无值
        /// </summary>
        public const long LongInterger = -1;//long.MinValue;

        /// <summary>
        /// Decimal数的默认无值
        /// </summary>
        public const decimal Decimal = -1;//decimal.MinValue;

        /// <summary>
        /// Float数的默认无值
        /// </summary>
        public const float Float = -1;//float.MinValue;

        /// <summary>
        /// Double数的默认无值
        /// </summary>
        public const double Double = -1;//double.MinValue;

        /// <summary>
        /// Byte数的默认无值
        /// </summary>
        public const byte Byte = byte.MinValue;

        /// <summary>
        /// SByte数的默认无值
        /// </summary>
        public const sbyte SByte = sbyte.MinValue;

        #endregion

        /// <summary>
        /// 字串的默认无值
        /// </summary>
        public static readonly string String = string.Empty;

        /// <summary>
        /// 一般对象的判断
        /// </summary>
        public const object Object = null;
    }

    #endregion

    #region 无值的扩展方法

    /// <summary>
    /// 无值的扩展方法
    /// </summary>
    public static class NullValueExtensions
    {
        #region 一般类型



        public static Guid TryToGuid(this string guid)
        {
            if (Guid.TryParse(guid, out var guid1))
            {
                return guid1;
            }
            return Guid.Empty;

        }

        public static string TryStringNull(this string value)
        {
            return value == null ? "" : value;

        }

        /// <summary>
        /// Object类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this object value)
        {
            if (value == null || value == NullValue.Object)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Object类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this object value)
        {
            return !value.IsNull();
        }
        public static bool IsGuidNotNull(this Guid? value)
        {
            return !value.IsGuidNull();
        }
        public static bool IsGuidNull(this Guid? value)
        {
            if (value == null || value == Guid.Empty)
            {
                return true;
            }

            return false;
        }
        /// <summary>
        /// String类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this string value)
        {
            if (string.IsNullOrEmpty(value) || string.IsNullOrWhiteSpace(value))
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// String类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this string value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// Guid类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this Guid value)
        {
            if (value == NullValue.Guid)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Guid类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this Guid value)
        {
            return !value.IsNull();
        }

        #region 数字类型

        /// <summary>
        /// ShortInterger类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this short value)
        {
            if (value == NullValue.ShortInterger)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// ShortInterger类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this short value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// Interger类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this int value)
        {
            if (value == NullValue.Interger)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Interger类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this int value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// LongInterger类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this long value)
        {
            if (value == NullValue.LongInterger)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// LongInterger类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this long value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// Decimal类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this decimal value)
        {
            if (value == NullValue.Decimal)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Decimal类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this decimal value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// Float类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this float value)
        {
            if (value == NullValue.Float)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Float类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this float value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// Double类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this double value)
        {
            if (value == NullValue.Double)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Double类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this double value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// Byte类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this byte value)
        {
            if (value == NullValue.Byte)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Byte类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this byte value)
        {
            return !value.IsNull();
        }

        /// <summary>
        /// SByte类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this sbyte value)
        {
            if (value == NullValue.SByte)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// SByte类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this sbyte value)
        {
            return !value.IsNull();
        }

        #endregion

        /// <summary>
        /// DateTime类型无值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否为空(true--真,false--假)</returns>
        public static bool IsNull(this DateTime value)
        {
            if (value == DateTime.MinValue || value <= NullValue.DateTime || value == NullValue.JavaScriptDateTime)
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// DateTime类型有值判断
        /// </summary>
        /// <param name="value">待判断对象</param>
        /// <returns>是否不为空(true--真,false--假)</returns>
        public static bool IsNotNull(this DateTime value)
        {
            return !value.IsNull();
        }

        #endregion
    }

    #endregion

}
