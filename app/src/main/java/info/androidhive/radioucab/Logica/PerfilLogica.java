package info.androidhive.radioucab.Logica;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import info.androidhive.radioucab.R;

public class PerfilLogica {

    private Context contexto;

    public Context getContexto() {
        return contexto;
    }
    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public Bitmap convertirImagenCirculo(Bitmap bitmap, int inicio) {
        try {
            final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);

            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            bitmap.recycle();

            return output;
        }
        catch (Exception ex) {
            if (inicio == 0) {
                Bitmap bitmapNuevo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.ic_imagen_perfil);
                return convertirImagenCirculo(bitmapNuevo, 1);
            }
            else
                return null;
        }
    }
}
