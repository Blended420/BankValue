package com.blended420.bankvalue;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.QuantityFormatter;

import javax.inject.Inject;
import java.awt.*;

public class BankValueOverlay extends Overlay {
    private final BankValueConfig config;
    private long haValue;
    private long geValue;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public BankValueOverlay(BankValueConfig config){
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.HIGH);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }
    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        String overlayTitle = "Bank Info:";
        String geValueTxt =   formatNumber(geValue);
        String haValueTxt = formatNumber(haValue);
        panelComponent.getChildren().add(TitleComponent.builder()
                .text(overlayTitle)
                .color(Color.GREEN)
                .build());

        panelComponent.setPreferredSize(new Dimension(
                geValue ==0 ? 90: graphics.getFontMetrics().stringWidth(geValueTxt) + 60,
                0));
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("GE:")
                    .right(geValueTxt)
                    .build());
            if(config.showHaValue()) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("HA:")
                        .right(haValueTxt)
                        .build());
            }

        return panelComponent.render(graphics);
    }

    public void updateInventoryValue(long geValue,long haValue) {
        this.geValue = geValue;
        this.haValue =haValue;
    }

    private String formatNumber(long num){

            return config.showPriceSuffix() ? QuantityFormatter.quantityToStackSize(num) : QuantityFormatter.formatNumber(num);

    }
}
