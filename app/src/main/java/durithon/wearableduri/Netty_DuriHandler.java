package durithon.wearableduri;

import android.content.Context;
import android.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by JJH on 2016-09-15.
 */
public class Netty_DuriHandler extends SimpleChannelInboundHandler<ByteBuf> {

    Context context;

    //final static String SENDMESAGGE = "passMessage";

    public Netty_DuriHandler(Context context){
        this.context = context;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {

    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        // Send the received message to all channels but the current one.
        // String a = bytebuf.toString(Charset.forName("UTF-8"));
        int bufcap = buf.capacity();

        byte[] bytemsg = new byte[bufcap];

        for (int i = 0; i < bufcap; i++) {
            bytemsg[i] = buf.getByte(i);
        }
        String str = new String(bytemsg, "UTF-8");
        Log.d("handlerString",""+str);

        String[] buffer = str.split(String.valueOf(MainActivity.ascii),2);

        final String buffer1 = buffer[1];
        switch (buffer[0]){

            /*
              음악재생 프로토콜
             */
            case "interestStart":
                //content = 녹음파일 포지션값
                MusicListUtil.startSong(context, buffer1);
//                passMessageToActivity("start");
                break;
            case "interestResume":
                //content = 녹음파일 포지션값
                MusicListUtil.resumeSong();
//                passMessageToActivity("resume");

                break;
            case "interestStop":
                MusicListUtil.stopSong();
                //content = 녹음파일 포지션값
//                passMessageToActivity("stop");
                break;
            /*
              녹음 프로토콜

             */
            case "record":
                //content = 녹음파일 포지션값

                break;


            /*
              만보계 프로토콜

             */
            case "pedometer":
                //content = 걸음수
                break;


            /*
          	위도경도  프로토콜

    		*/
            case "latlon":
                //content = 위도/경도


                break;

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

//    private void passMessageToActivity(String message){
//        Intent intent = new Intent();
//        intent.setAction(SENDMESAGGE);
//        intent.putExtra("message",message);
//        context.sendBroadcast(intent);
//    }
}
