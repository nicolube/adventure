package net.kyori.adventure.text.serializer.gson;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.HoverEvent.showEntity;
import static net.kyori.adventure.text.event.HoverEvent.showItem;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ComponentSerializationBenchmark {

  @Benchmark
  public String simpleComponent() {
    return gson().serialize(text("Hello, World!", style(TextDecoration.UNDERLINED)));
  }

  @Benchmark
  public String componentTreeWithStyle() {
    return gson().serialize(text()
                              .decorate(TextDecoration.UNDERLINED, TextDecoration.ITALIC)
                              .append(text("Component ", color(0x8cfbde)))
                              .append(text("with ", color(0x0fff95), TextDecoration.BOLD))
                              .append(text("hex ", color(0x06ba63)))
                              .append(text("colors", color(0x103900)))
                              .build());
  }

  @Benchmark
  public String componentTreeWithEvents() {
    return gson().serialize(text()
                              .decorate(TextDecoration.UNDERLINED, TextDecoration.ITALIC)
                              .append(text("Component ", color(0x8cfbde))
                                        .clickEvent(openUrl("https://kyori.net/")))
                              .append(text("with ", color(0x0fff95), TextDecoration.BOLD)
                                        .hoverEvent(showItem(Key.key("iron_sword"), 1, BinaryTagHolder.of("{Damage: 30, RepairCost: 4, Enchantments: [{id: 'minecraft:sharpness', lvl: 3s}, {id: 'minecraft:unbreaking', lvl: 1s}]}"))))
                              .append(text("hex ", color(0x06ba63))
                                        .hoverEvent(showEntity(Key.key("pig"), UUID.randomUUID(), text("Piggy", NamedTextColor.YELLOW))))
                              .append(text("colors", color(0x103900))
                                        .hoverEvent(showText(text("Text hover!"))))
                              .build());
  }
}
