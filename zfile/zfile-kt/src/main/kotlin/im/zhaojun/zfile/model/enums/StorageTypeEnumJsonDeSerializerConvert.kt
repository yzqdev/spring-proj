package im.zhaojun.zfile.model.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException

/**
 * @author zhaojun
 */
class StorageTypeEnumJsonDeSerializerConvert : JsonDeserializer<StorageTypeEnum?>() {
    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): StorageTypeEnum? {
        return StorageTypeEnum.Companion.getEnum(jsonParser.text)
    }
}