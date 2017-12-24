package net.openhft.hashing;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashValCompareTest {

        @Test
        public void HashValCompareTest() {
            ByteBuffer sequence = ByteBuffer.allocate(128);
            sequence.order(ByteOrder.LITTLE_ENDIAN);

            sequence.putLong(0, 1 + 0xBA79074579D4BAFL);
            sequence.putLong(32, 2 + 0x9C90005B82134000L);
            long h1 = LongHashFunction.xx().hashBytes(sequence);

            sequence.putLong(0, 1 + 0xBA79074579D4BAFL);
            sequence.putLong(32, 2 + 0x9C90005B82134000L);
            long h2 = LongHashFunction.city_1_1().hashBytes(sequence);

            assertNotEquals(h1, h2);

            sequence.putLong(0, 1 + 0xBA79074579D4BAFL);
            sequence.putLong(32, 2 + 0x9C90005B82134000L);
            h1 = LongHashFunction.city_1_1().hashBytes(sequence);

            sequence.putLong(0, 1 + 0xBA79074579D4BAFL);
            sequence.putLong(32, 2 + 0x9C90005B82134000L);
            h2 = LongHashFunction.farmNa().hashBytes(sequence);

            assertNotEquals(h1, h2);

            sequence.putLong(0, 1 + 0xBA79074579D4BAFL);
            sequence.putLong(32, 2 + 0x9C90005B82134000L);
            h1 = LongHashFunction.farmUo().hashBytes(sequence);

            sequence.putLong(0, 1 + 0xBA79074579D4BAFL);
            sequence.putLong(32, 2 + 0x9C90005B82134000L);
            h2 = LongHashFunction.murmur_3().hashBytes(sequence);

            assertNotEquals(h1, h2);
        }

}
