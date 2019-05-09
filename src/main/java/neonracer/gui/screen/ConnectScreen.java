package neonracer.gui.screen;

import neonracer.gui.annotation.BindWidget;
import neonracer.gui.annotation.EventHandler;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.ClickEvent;
import neonracer.gui.widget.Button;
import neonracer.gui.widget.TextBox;
import neonracer.network.proto.Login;
import neonracer.render.RenderContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;

@LayoutFile("guis/connect.xml")
public class ConnectScreen extends Screen {

    @BindWidget("tbIpAddress")
    public TextBox ipAddressBox;

    @BindWidget("tbNickname")
    public TextBox nicknameBox;

    @BindWidget("btnConnect")
    public Button btnConnect;

    private boolean connecting;

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        EventBus.getDefault().register(this);
    }

    @EventHandler("btnConnect")
    public void onConnect(ClickEvent clickEvent) {
        if (connecting)
            return;

        String ip = ipAddressBox.getText();
        connecting = true;
        btnConnect.setText("Verbinden...");
        new Thread(() -> {
            boolean connected = context.getClient().connect(ip);
            if (!connected) {
                JOptionPane.showMessageDialog(null, "Verbindung zum Server konnte nicht hergestellt werden.");
                btnConnect.setText("Verbinden");
                connecting = false;
            } else {
                context.getClient().send(Login.LoginRequest.newBuilder().setNickname(nicknameBox.getText()).build());
            }
        }).start();
    }

    @Subscribe
    public void onResponse(Login.LoginResponse response) {
        if (response.getStatus() == Login.LoginStatus.NICKNAME_TAKEN) {
            JOptionPane.showMessageDialog(null, "Entschuldigung, dieser Nickname ist bereits vergeben.");
            btnConnect.setText("Verbinden");
            connecting = false;
        } else {
            parent.close(this);
            parent.show(new MainScreen());
        }
    }


}
