package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.resources.I18n
import org.lwjgl.opengl.GL11
import java.awt.Color

class GuiMainMenu : GuiScreen(), GuiYesNoCallback {
    override fun initGui() {
        if(LiquidBounce.latestVersion.isNotEmpty()&&!LiquidBounce.displayedUpdateScreen){
            mc.displayGuiScreen(GuiUpdate())
            return
        }

        val defaultHeight = (this.height / 3.5).toInt()

        this.buttonList.add(GuiButton(1, this.width / 2 - 75, defaultHeight, 150, 25, I18n.format("menu.singleplayer")))
        this.buttonList.add(GuiButton(2, this.width / 2 - 75, defaultHeight + 30, 150, 25, I18n.format("menu.multiplayer")))
        this.buttonList.add(GuiButton(100, this.width / 2 - 75, defaultHeight + 30*2, 150, 25, "%ui.altmanager%"))
        this.buttonList.add(GuiButton(0, this.width / 2 - 75, defaultHeight + 30*3, 150, 25, I18n.format("menu.options")))
        this.buttonList.add(GuiButton(4, this.width / 2 - 75, defaultHeight + 30*4, 150, 25, I18n.format("menu.quit")))

        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        val bHeight=(this.height / 3.5).toInt()

        //Gui.drawRect(width / 2 - 60, bHeight - 30, width / 2 + 60, bHeight + 174, Integer.MIN_VALUE)

        Fonts.font60.drawCenteredString(LiquidBounce.CLIENT_NAME,(width / 2).toFloat(), (bHeight - 30).toFloat(),Color.WHITE.rgb,false)
        Fonts.font40.drawString(LiquidBounce.CLIENT_VERSION+if(LiquidBounce.latestVersion.isNotEmpty()){" §c-> §a"+LiquidBounce.latestVersion}else{""}
            , 3F, (height - Fonts.font35.FONT_HEIGHT).toFloat(), 0xffffff,  false)
        val str="Hello "+ Minecraft.getMinecraft().getSession().username + "!"
        Fonts.font40.drawString(str, (this.width - Fonts.font40.getStringWidth(str) - 3).toFloat(), (height - Fonts.font35.FONT_HEIGHT).toFloat(), 0xffffff, false)
        super.drawScreen(mouseX, mouseY, partialTicks)

        GL11.glPushMatrix()
        GL11.glTranslatef(2f,2f,0f)

        for (jsonElement in LiquidBounce.updatelog) {
            try {
                if(jsonElement.isJsonObject){
                    val json=jsonElement.asJsonObject
                    Fonts.font35.drawString(json.get("text").asString, 0f, 0f, ColorUtils.decodeColorJsonFormat(json.getAsJsonObject("color")).rgb)
                }else{
                    Fonts.font35.drawString(jsonElement.asString, 0f, 0f, Color(255, 255, 255, 160).rgb)
                }
                GL11.glTranslatef(0f,Fonts.font35.height+1f,0f)
            }catch (e: UnsupportedOperationException){
                // ignore
            }
        }

        GL11.glPopMatrix()
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            1 -> mc.displayGuiScreen(GuiSelectWorld(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            4 -> mc.shutdown()
            100 -> mc.displayGuiScreen(GuiAltManager(this))
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}