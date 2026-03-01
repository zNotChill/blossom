import me.znotchill.blossom.instances.InstanceTemplate
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

object BaseGenerator : InstanceTemplate {
    override fun generate(unit: GenerationUnit) {
        unit.modifier().fill(
            unit.absoluteStart(),
            unit.absoluteEnd().withY(1.0),
            Block.BEDROCK
        )
    }
}