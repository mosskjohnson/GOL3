package util;

public class Camera {

    public double offsetX;
    public double offsetY;
    public double zoomX;
    public double zoomY;
    public final double INITIAL_ZOOM;
    public final double MIN_ZOOM;
    public final double MAX_ZOOM;

    public Camera(double initialOffsetX, double initialOffsetY, double initialZoom, double minZoom, double maxZoom) {
        this.offsetX = initialOffsetX;
        this.offsetY = initialOffsetY;

        this.INITIAL_ZOOM = initialZoom;
        this.MIN_ZOOM = minZoom;
        this.MAX_ZOOM = maxZoom;
        this.zoomX = initialZoom;
        this.zoomY = initialZoom;
    }

    //

    public int worldToScreenX(int worldX) {
        return (int) ((worldX - offsetX) * zoomX);
    }

    public double screenToWorldX(int screenX) {
        return (double) ((screenX / zoomX) + offsetX);
    }

    public int worldToScreenY(int worldY) {
        return (int) ((worldY - offsetY) * zoomY);
    }

    public double screenToWorldY(int screenY) {
        return (double) ((screenY / zoomY) + offsetY);
    }

    public int mouseToWorldX(int x) {
        x = (int) (screenToWorldX(x));
        if (x < 0) {
            x--;
        }
        return x;
    }

    public int mouseToWorldY(int y) {
        y = (int) (screenToWorldY(y));
        if (y < 0) {
            y--;
        }
        return y;
    }

    public void zoom(double factor) {
        zoomX = UtilMath.clamp(zoomX * factor, MIN_ZOOM, MAX_ZOOM);
        zoomY = UtilMath.clamp(zoomY * factor, MIN_ZOOM, MAX_ZOOM);
    }
    
    public double[] getBounds(int screenWidth, int screenHeight) {
        double left = screenToWorldX(0) - 1;
        double top = screenToWorldY(0) - 1;
        double right = screenToWorldX(screenWidth);
        double bottom = screenToWorldY(screenHeight);
        return new double[] {left, top, right, bottom};
    }
}
