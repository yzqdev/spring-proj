package im.zhaojun.zfile.model.enums

import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author zhaojun
 */
@Converter(autoApply = true)
class StorageTypeEnumConvert : AttributeConverter<StorageTypeEnum?, String?> {
    override fun convertToDatabaseColumn(attribute: StorageTypeEnum?): String? {
        return attribute.getKey()
    }

    override fun convertToEntityAttribute(dbData: String?): StorageTypeEnum? {
        return StorageTypeEnum.Companion.getEnum(dbData)
    }
}