/**
 *  @file       init.sc 
 *
 *              Top-level initialization for the "Seas of the Subconscious"
 *              soundscape.
 *
 *  @author     Colin Sullivan <colinsul [at] gmail.com>
 *
 *              Copyright (c) 2012 Colin Sullivan
 *              Licensed under the GPLv3 license.
 **/

({
  var m,
    mBounds,
    projRoot = "/Users/colin/Projects/seas-of-subconscious",
    projSfx = projRoot +/+ "seas-of-subconscious-samples/";

  s.quit;
  
  s.options.memSize = 262144; // 256 Mb
  s.options.inDevice = "JackRouter";
  s.options.outDevice = "JackRouter";
  s.options.numOutputBusChannels = 16;
  s.options.blockSize = 8;
  s.boot();
  m = s.meter();


  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);

  Instr.dir = projRoot +/+ "lib/";
  Instr.loadAll();

  s.doWhenBooted({
    var soundsOfSubConsciousSeas, bufsToLoad, bufManager;

    soundsOfSubConsciousSeas = SoundsOfSubconsciousSeas.new();
    bufManager = BufferManager.new();
    
    soundsOfSubConsciousSeas.init((
      bufManager: bufManager,
      outbus: 8
    ));

    bufsToLoad = [
      ["58411__sinatra314__shorewaves1004.wav", \splashingWaterBuf],
      ["40563__genghis-attenborough__sedge-warbler-call.wav", \warblerBuf],
      ["59492__dobroide__20080807-seagulls.wav", \gullsBuf],
      ["101381__benboncan__creaking-floorboard.wav", \creakingFloorboardBuf],
      ["163300__laurent__canadian-loons-in-family.wav", \loonsBuf],
      ["32304__acclivity__shipsbell.wav", \shipsBellBuf],
      ["Inventions For Radio - Dreams" +/+ "02 The Dreams - 1. Running-filtered.aif", \derbyshireRunningBuf],
      ["melodic-bass" +/+ "avadhuta-01.aif", \avadhuta01Buf],
      ["melodic-bass" +/+ "avadhuta-02.aif", \avadhuta02Buf],
      ["melodic-bass" +/+ "descending-dissonant-chord.aif", \descendingDissonantBuf],
      ["melodic-bass" +/+ "triads.aif", \triadsBuf],
      ["88449__davidou__baleines.wav", \baleinesBuf],
      ["173979-mega-forghorn-windowed.aif", \fogHornBuf],
      ["87785_long-scary-foghorn.aif", \fogHorn02Buf],
      ["shanty_01.aif", \seaShanty01],
      ["long_shanty_01.aif", \seaShanty02],
      ["shanty_03.aif", \seaShanty03],
      ["long_shanty_02.aif", \seaShanty04]
    ];

    bufManager.init((
      rootDir: projSfx,
      doneLoadingCallback: {
        "done loading!".postln();

        soundsOfSubConsciousSeas.start_soundscape();
      }
    ));
    bufManager.load_bufs(bufsToLoad);
    
  });

}.value();)

