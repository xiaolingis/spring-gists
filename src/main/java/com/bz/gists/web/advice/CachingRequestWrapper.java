package com.bz.gists.web.advice;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created on 2019/7/19
 *
 * @author zhongyongbin
 */
public class CachingRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] payload;

    public CachingRequestWrapper(final HttpServletRequest request) throws IOException {
        super(request);
        this.payload = IOUtils.toByteArray(getRequest().getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachingServletInputStream(payload);
    }

    private static final class CachingServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream bis;

        CachingServletInputStream(byte[] payload) {
            bis = new ByteArrayInputStream(payload);
        }

        @Override
        public int read() {
            return bis.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            //
        }
    }

}
