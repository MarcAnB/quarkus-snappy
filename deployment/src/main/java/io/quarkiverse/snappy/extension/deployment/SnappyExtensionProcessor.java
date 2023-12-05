package io.quarkiverse.snappy.extension.deployment;

import io.quarkiverse.snappy.extension.runtime.SnappyRecorder;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class SnappyExtensionProcessor {

    private static final String FEATURE = "snappy-extension";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    public void build(BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
            BuildProducer<NativeImageResourceBuildItem> nativeLibs) {
        reflectiveClass.produce(ReflectiveClassBuildItem.builder("org.xerial.snappy.SnappyInputStream",
                "org.xerial.snappy.SnappyOutputStream").methods().fields().build());

        String root = "org/xerial/snappy/native/";
        String dir = "Linux/x86_64";
        String snappyNativeLibraryName = "libsnappyjava.so";
        String path = root + dir + "/" + snappyNativeLibraryName;
        nativeLibs.produce(new NativeImageResourceBuildItem(path));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void loadSnappyIfEnabled(SnappyRecorder recorder) {
        recorder.loadSnappy();
    }
}
