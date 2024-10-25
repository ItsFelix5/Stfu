package stfu;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

public class Options {
    public static final SimpleOption<Integer> maxChatHistory = new SimpleOption<>(
            "options.maxChatHistory",
            SimpleOption.emptyTooltip(),
            GameOptions::getGenericValueText,
            new SimpleOption.ValidatingIntSliderCallbacks(10, 5000),
            Codec.intRange(10, 5000),
            100,
            value -> {
            }
    );

    public static final SimpleOption<Boolean> announceAdvancements = SimpleOption.ofBoolean("options.announceAdvancements", true);

    public static final SimpleOption<AdminChat> adminChat = new SimpleOption<>(
            "options.adminChat",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.translatable("options.adminChat." + value.name().toLowerCase()),
            new SimpleOption.PotentialValuesBasedCallbacks<>(List.of(AdminChat.values()), Codec.INT.xmap(i -> AdminChat.values()[i],
                    AdminChat::ordinal)),
            AdminChat.ENABLED,
            value -> {
            }
    );
    public static final SimpleOption<CompactChat> compactChat = new SimpleOption<>(
            "options.compactChat",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.translatable("options.compactChat." + value.name().toLowerCase()),
            new SimpleOption.PotentialValuesBasedCallbacks<>(List.of(CompactChat.values()), Codec.INT.xmap(i -> CompactChat.values()[i],
                    CompactChat::ordinal)),
            CompactChat.ONLY_CONSECUTIVE,
            value -> {
            }
    );
    public static final Map<String, SimpleOption<?>> OPTIONS = Map.of(
            "maxChatHistory", maxChatHistory,
            "announceAdvancements", announceAdvancements,
            "adminChat", adminChat,
            "compactChat", compactChat
    );

    public enum AdminChat {
        ENABLED,
        ONLY_PLAYERS,
        DISABLED
    }

    public enum CompactChat {
        ALL,
        ONLY_CONSECUTIVE,
        NEVER
    }
}
