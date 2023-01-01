package im.zhaojun.zfile.model.enums

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException

/**
 * @author zhaojun
 */
class StorageTypeEnumSerializerConvert : JsonSerializer<StorageTypeEnum>() {
    @Throws(IOException::class)
    override fun serialize(
        storageTypeEnum: StorageTypeEnum,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeString(storageTypeEnum.key)
    }
}