package im.zhaojun.zfile.model.enums

import org.springframework.core.convert.converter.Converter
import org.springframework.lang.NonNull

/**
 * @author zhaojun
 */
class StorageTypeEnumDeSerializerConvert : Converter<String?, StorageTypeEnum?> {
    override fun convert(@NonNull s: String?): StorageTypeEnum? {
        return StorageTypeEnum.Companion.getEnum(s)
    }
}