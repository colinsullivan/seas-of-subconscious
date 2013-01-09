Quarks.gui;

({
  var m,
    mBounds,
    projRoot = "/Users/colin/Projects/seas-of-subconscious",
    projSfx = projRoot +/+ "raw-samples";

  s.quit;
  
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  /*s.options.inDevice = "SF + 1818";*/
  /*s.options.inDevice = "AudioBox 1818 VSL ";*/
  /*s.options.inDevice = "Soundflower (64ch)";*/

  s.options.outDevice = "Soundflower (64ch)";
  s.options.sampleRate = 48000;
  s.options.blockSize = 8;
  s.boot();
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);

  Instr.dir = projRoot +/+ "lib/";
  Instr.loadAll();

  s.doWhenBooted({
    var soundsOfSubConsciousSeas;

    soundsOfSubConsciousSeas = SoundsOfSubconsciousSeas.new();
    soundsOfSubConsciousSeas.init();

    soundsOfSubConsciousSeas.load_buf(
      projSfx +/+ "58411__sinatra314__shorewaves1004.wav",
      \splashingWaterBuf
    );

    soundsOfSubConsciousSeas.load_buf(
      projSfx +/+ "40563__genghis-attenborough__sedge-warbler-call.wav",
      \warblerBuf
    );

    soundsOfSubConsciousSeas.load_buf(
      projSfx +/+ "59492__dobroide__20080807-seagulls.wav",
      \gullsBuf
    );

    soundsOfSubConsciousSeas.load_buf(
      projSfx +/+ "101381__benboncan__creaking-floorboard.wav",
      \creakingFloorboardBuf
    );

    soundsOfSubConsciousSeas.load_buf(
      projSfx +/+ "163300__laurent__canadian-loons-in-family.wav",
      \loonsBuf
    );
  
  });


}.value();)

