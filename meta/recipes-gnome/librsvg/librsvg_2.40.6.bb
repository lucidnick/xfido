SUMMARY = "Library for rendering SVG files"
HOMEPAGE = "http://ftp.gnome.org/pub/GNOME/sources/librsvg/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://rsvg.h;beginline=3;endline=24;md5=20b4113c4909bbf0d67e006778302bc6"

SECTION = "x11/utils"
DEPENDS = "cairo gdk-pixbuf glib-2.0 libcroco libxml2 pango"
BBCLASSEXTEND = "native"

inherit autotools pkgconfig gnomebase gtk-doc pixbufcache

GNOME_COMPRESS_TYPE = "xz"

SRC_URI += "file://gtk-option.patch \
            file://CVE-2015-7558_1.patch \
            file://CVE-2015-7558_2.patch \
            file://CVE-2015-7558_3.patch \
            "

SRC_URI[archive.md5sum] = "259fd160b47ec11f3c27d7e18e507c99"
SRC_URI[archive.sha256sum] = "8af349f241677b04b7a1ea6b9b33a6343e781bcccc8a09d00208a47342584f06"

EXTRA_OECONF = "--disable-introspection --disable-vala"

# The older ld (2.22) on the host (Centos 6.5) doesn't have the
# -Bsymbolic-functions option, we can disable it for native.
EXTRA_OECONF_append_class-native = " --enable-Bsymbolic=auto"

PACKAGECONFIG ??= "gdkpixbuf"
# The gdk-pixbuf loader
PACKAGECONFIG[gdkpixbuf] = "--enable-pixbuf-loader,--disable-pixbuf-loader,gdk-pixbuf-native"
# GTK+ test application (rsvg-view)
PACKAGECONFIG[gtk] = "--with-gtk3,--without-gtk3,gtk+3"

do_install_append() {
	# Loadable modules don't need .a or .la on Linux
	rm -f ${D}${libdir}/gdk-pixbuf-2.0/*/loaders/*.a ${D}${libdir}/gdk-pixbuf-2.0/*/loaders/*.la
}

PACKAGES =+ "librsvg-gtk rsvg"
FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-dbg += "${libdir}/gdk-pixbuf-2.0/*/loaders/.debug"
FILES_rsvg = "${bindir}/rsvg* \
	      ${datadir}/pixmaps/svg-viewer.svg \
	      ${datadir}/themes"
FILES_librsvg-gtk = "${libdir}/gdk-pixbuf-2.0/*/*/*.so"

PIXBUF_PACKAGES = "librsvg-gtk"

PIXBUFCACHE_SYSROOT_DEPS_append_class-native = " harfbuzz-native:do_populate_sysroot_setscene pango-native:do_populate_sysroot_setscene icu-native:do_populate_sysroot_setscene"
